package com.unihub.auth.accountmanagement.service.impl;

import com.unihub.auth.accountmanagement.dto.request.BaseUserRequest;
import com.unihub.auth.accountmanagement.dto.request.UpdateUserRequest;
import com.unihub.auth.accountmanagement.dto.response.UserMetaDataResponse;
import com.unihub.auth.accountmanagement.job.dto.JobResultResponse;
import com.unihub.auth.accountmanagement.service.IAccountManagementService;
import com.unihub.auth.accountmanagement.strategy.UserCreationStrategy;
import com.unihub.auth.accountmanagement.strategy.UserRoles;
import com.unihub.auth.accountmanagement.strategy.resolver.StrategyResolver;
import com.unihub.auth.internal.client.SubscriptionFeignClient;
import com.unihub.auth.internal.client.UniversityFeignClient;
import com.unihub.auth.internal.dto.response.SubscriptionPlanResponseDto;
import com.unihub.auth.internal.dto.response.UniversityResponse;
import com.unihub.auth.security.dto.response.UserDto;
import com.unihub.auth.security.mapper.UserMapper;
import com.unihub.auth.security.model.User;
import com.unihub.auth.security.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class AccountManagementServiceImpl implements IAccountManagementService {

    private final Job job;

    private final JobOperator operator;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final StrategyResolver strategyResolver;

    private final UniversityFeignClient universityFeignClient;

    private final SubscriptionFeignClient subscriptionFeignClient;

    @Override
    @Transactional
    public UserDto createUser(BaseUserRequest userRequest, Authentication authentication) {
        UUID tid=UUID.fromString(Objects.requireNonNull(authentication.getDetails()).toString());
        long currentPlanMaxUserAmount=handleRequest(tid);
        long currentUserCount= userRepository.countByUniversityTid(tid);
        if(currentPlanMaxUserAmount-currentUserCount<=0){
            throw new AccessDeniedException("You have exceeded the maximum allowed users amount,please upgrade your plan to proceed this operation");
        }
        UserRoles role = userRequest.getRoleName();
        UserCreationStrategy strategy = strategyResolver.resolve(role);
        return strategy.createUser(userRequest, authentication);
    }


    @Override
    public JobResultResponse insertFromCsv(MultipartFile file, Authentication authentication) throws Exception{
        UUID tid=UUID.fromString(Objects.requireNonNull(authentication.getDetails()).toString());
        long currentPlanMaxUserAmount=handleRequest(tid);
        long currentUserCount= Math.toIntExact(userRepository.countByUniversityTid(tid));
        long allowedUsersToInsert=currentPlanMaxUserAmount-currentUserCount;
        if(allowedUsersToInsert<=0) {
            throw new AccessDeniedException("You have exceeded the maximum allowed users amount,please upgrade your plan to proceed this operation");
        }
        JobParameters jobParameters=new JobParametersBuilder()
                .addLong("StartAt",System.currentTimeMillis())
                .addString("csvFilePath",validatedAndSaveFile(file,allowedUsersToInsert).toAbsolutePath().toString())
                .addString("tid", Objects.requireNonNull(authentication.getDetails()).toString())
                .toJobParameters();
        JobExecution jobExecution=operator.start(job,jobParameters);
        return getJobResult(jobExecution);
    }

    @Override
    public UserDto getUser(UUID id, Authentication authentication) {
        return userMapper.toDto(fetchUser(id,authentication));
    }

    @Override
    public List<UserMetaDataResponse> getUsers(int pageNum, String sortDir, String sortField, UserRoles roleName, UUID uuid, Authentication authentication) {
        Pageable pageable=createPageable(pageNum,sortDir,sortField);
        return userRepository.getUsers(
                roleName !=null? roleName.name() : null
                ,uuid
                , UUID.fromString(Objects.requireNonNull(authentication.getDetails()).toString())
                ,authentication.getName()
                , pageable
        ).getContent();
    }

    @Override
    public List<UserMetaDataResponse> searchUser(String searchText) {
        return userRepository.searchUsers(searchText)
                .stream()
                .map(userMapper::toMetadata).toList();
    }

    @Override
    public UserDto updateUser(UUID id, UpdateUserRequest userRequest,Authentication authentication){
        User user=fetchUser(id, authentication);
        updateUser(user,userRequest);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto updateGpa(UUID id, Double gpa, Authentication authentication) {
        User user=fetchUser(id, authentication) ;
        if(!user.getRole().getName().equals("ROLE_STUDENT")){
            throw new IllegalArgumentException("This user doesn't have a gpa to update");
        }
        user.getUniversityMetadata().setGpa(gpa);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(UUID id, Authentication authentication) {
        userRepository.delete(fetchUser(id,authentication));
    }

    @Override
    @Transactional
    public void deleteUsers(List<UUID> ids) {
        userRepository.deleteAllById(ids);
    }

    private User fetchUser(UUID id,Authentication authentication){
        return userRepository
                .findByUidAndUniversityMetadata_Tid(id,UUID.fromString(Objects.requireNonNull(authentication.getDetails()).toString()))
                .orElseThrow(()->new EntityNotFoundException("No user found with id"+id));
    }

    private Path validatedAndSaveFile(MultipartFile file,long maxRows) throws IOException {
        if (file.isEmpty()){
            throw new IllegalArgumentException("CSV file is empty");
        }
        String contentType = file.getContentType();
        if (!"text/csv".equals(contentType) && !"application/vnd.ms-excel".equals(contentType)) {
            throw new IllegalArgumentException("Invalid file type. Only CSV is allowed.");
        }
         countCsvRowsUpToLimit(file,maxRows);
        Path tempFile = Files.createTempFile("users-"+UUID.randomUUID(), ".csv");
        file.transferTo(tempFile.toFile());
        return tempFile;
    }

    private void countCsvRowsUpToLimit(MultipartFile file, long maxRows) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            long count = 0;
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                count++;
                if (count > maxRows) {
                    throw new AccessDeniedException("Csv rows count exceeds the maximum allowed number of users:"+maxRows);
                }
            }
        }
    }

    private JobResultResponse getJobResult(JobExecution jobExecution){
        StepExecution step = jobExecution.getStepExecutions().iterator().next();
        return JobResultResponse.builder()
                .status(jobExecution.getStatus().toString())
                .totalRead(step.getReadCount())
                .inserted(step.getWriteCount())
                .failed(step.getSkipCount())
                .processFailures(step.getProcessSkipCount())
                .writeFailures(step.getWriteSkipCount())
                .startTime(jobExecution.getStartTime())
                .endTime(jobExecution.getEndTime())
                .build();
    }

    private Pageable createPageable(int pageNum, String sortDir, String sortField){
        int pageSize=10;
        String mappedField = switch (sortField) {
            case "roleName" -> "role.name";
            case "cid" -> "universityMetadata.cid";
            default -> sortField;
        };
        return  PageRequest.of(
                pageNum-1,
                pageSize,
                sortDir.equalsIgnoreCase("asc")? Sort.by(mappedField).ascending():Sort.by(mappedField).descending()
        );
    }

    private void updateUser(User user,UpdateUserRequest userRequest){
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setGender(userRequest.getGender());
        user.setDob(userRequest.getDob());
        if(!user.getRole().getName().equals("SYSTEM_ADMIN")&&userRequest.getCid()!=null){
            user.getUniversityMetadata().setCid(userRequest.getCid());
        }
    }


    private long handleRequest(UUID tid) {
        UniversityResponse university = fetchUniversityResponse(tid)
                .orElseThrow(() -> new EntityNotFoundException("University entity not found, Service might not be available at the moment try again later "));
        if(university.getSubscriptionPlan()==null){
            throw new AccessDeniedException("No Subscription Plan is set please set a plan");
        }
        SubscriptionPlanResponseDto sDto=fetchUniversitySubscriptionPlan(university.getSubscriptionPlan().getPid())
                .orElseThrow( ()-> new EntityNotFoundException("Subscription entity not found or Service might not be available at the moment try again later "));
        return sDto.getMaxUserAmount();
    }

    private Optional<UniversityResponse> fetchUniversityResponse(UUID tid) {
        return Optional.ofNullable(universityFeignClient.fetchUniversity(tid).getBody());
    }

    private Optional<SubscriptionPlanResponseDto> fetchUniversitySubscriptionPlan(UUID pid){
        return Optional.ofNullable(subscriptionFeignClient.getSubscriptionPlan(pid).getBody());
    }

}
