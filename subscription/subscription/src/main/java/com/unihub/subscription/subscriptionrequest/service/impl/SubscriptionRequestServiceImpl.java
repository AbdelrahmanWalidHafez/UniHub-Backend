package com.unihub.subscription.subscriptionrequest.service.impl;

import com.unihub.subscription.subscriptionrequest.client.UniversityFeignClient;
import com.unihub.subscription.subscriptionrequest.dto.request.DeleteFileRequest;
import com.unihub.subscription.subscriptionrequest.dto.request.SubscriptionRequestDto;
import com.unihub.subscription.subscriptionrequest.dto.request.UpdateSubscriptionRequestDto;
import com.unihub.subscription.subscriptionrequest.dto.request.UploadFileRequest;
import com.unihub.subscription.subscriptionrequest.dto.response.AfterUpdateResponse;
import com.unihub.subscription.subscriptionrequest.dto.response.SubscriptionsMetaData;
import com.unihub.subscription.subscriptionrequest.dto.response.SubscriptionRequestResponseDto;
import com.unihub.subscription.subscriptionrequest.dto.response.UniversityResponse;
import com.unihub.subscription.subscriptionrequest.mapper.SubscriptionMapper;
import com.unihub.subscription.subscriptionrequest.model.Status;
import com.unihub.subscription.subscriptionrequest.model.SubscriptionRequest;
import com.unihub.subscription.subscriptionrequest.repository.SubscriptionRequestRepository;
import com.unihub.subscription.subscriptionrequest.service.ISubscriptionRequestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SubscriptionRequestServiceImpl implements ISubscriptionRequestService {

   
    private final StreamBridge streamBridge;
   
    private final SubscriptionMapper subscriptionMapper;
    
    private final UniversityFeignClient universityFeignClient;

    private final SubscriptionRequestRepository subscriptionRequestRepository;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png"
    );

    @Override
    public SubscriptionRequestResponseDto createSubscriptionRequest(SubscriptionRequestDto subscriptionRequestDto, MultipartFile accreditation, MultipartFile logo) throws IOException {
        SubscriptionRequest subscriptionRequest =generateEntity(subscriptionRequestDto);
        SubscriptionRequest savedSubscription=subscriptionRequestRepository.save(subscriptionRequest);
        uploadFiles(savedSubscription,accreditation,logo);
        return subscriptionMapper.toDto(subscriptionRequest);
    }

    @Override
    public SubscriptionRequestResponseDto getSubscription(UUID id) {
        SubscriptionRequest subscriptionRequest= fetchRequest(id);
        return subscriptionMapper.toDto(subscriptionRequest);
    }

    @Override
    public List<SubscriptionsMetaData> getSubscriptionRequests(int pageNum, String sortDir, String sortField, Status status){
        Pageable pageable=createPageable(pageNum,sortDir,sortField);
        if (status!=null&&Arrays.asList(Status.values()).contains(status)){
            return subscriptionRequestRepository
                    .findAllByStatus(status,pageable)
                    .stream()
                    .map(subscriptionMapper::toMetaData)
                    .toList();
        }else {
            return subscriptionRequestRepository
                    .findAll(pageable)
                    .stream()
                    .map(subscriptionMapper::toMetaData)
                    .toList();
        }
    }

    @Override
    @Transactional
    public void deleteSubscriptionRequest(UUID id) {
        SubscriptionRequest subscriptionRequest= fetchRequest(id);
        if(subscriptionRequest.getStatus().equals(Status.REJECTED)){
            deleteFiles(subscriptionRequest);
        }
        subscriptionRequestRepository.delete(subscriptionRequest);
    }

    @Override
    @Transactional
    public AfterUpdateResponse updateSubscriptionStatus(UpdateSubscriptionRequestDto updateSubscriptionRequestDto, UUID id) {
        SubscriptionRequest subscriptionRequest=fetchRequest(id);
        return handleUpdateRequest(subscriptionRequest,updateSubscriptionRequestDto);
    }

    @Override
    public void uploadFile(UploadFileRequest uploadFileRequest){
        streamBridge.send("uploadFile-out-0",uploadFileRequest);
    }

    @Override
    public void deleteFile(DeleteFileRequest deleteFileRequest){
        streamBridge.send("deleteFile-out-0",deleteFileRequest);
    }


    private SubscriptionRequest fetchRequest(UUID id){
       return  subscriptionRequestRepository
                .findById(id)
                .orElseThrow(()->new EntityNotFoundException("Subscription Request not found"));
    }

    private SubscriptionRequest generateEntity(SubscriptionRequestDto subscriptionRequestDto){
        SubscriptionRequest subscriptionRequest = subscriptionMapper.toEntity(subscriptionRequestDto);
        subscriptionRequest.setAccreditationKey(generateFileKey());
        subscriptionRequest.setUniversityLogoKey(generateFileKey());
        return subscriptionRequest;
    }

    private String generateFileKey(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    private void uploadFiles(SubscriptionRequest subscriptionRequest,MultipartFile accreditation, MultipartFile logo) throws IOException  {
        if(isInvalidValidContentType(accreditation) || isInvalidValidContentType(logo)){
            throw new IllegalArgumentException("Invalid content type");
        }
        UploadFileRequest uploadFileRequest=UploadFileRequest.builder()
                .fileContent(Base64.getEncoder().encodeToString(accreditation.getBytes()))
                .key(subscriptionRequest.getAccreditationKey())
                .contentType(accreditation.getContentType())
                .build();
        uploadFile(uploadFileRequest);
        uploadFileRequest=UploadFileRequest.builder()
                .fileContent(Base64.getEncoder().encodeToString(logo.getBytes()))
                .key(subscriptionRequest.getUniversityLogoKey())
                .contentType(logo.getContentType())
                .build();
        uploadFile(uploadFileRequest);
    }

    private Pageable createPageable(int pageNum, String sortDir,String sortField){
        int pageSize=10;
       return  PageRequest.of(
                pageNum-1,
                pageSize,
                sortDir.equalsIgnoreCase("asc")? Sort.by(sortField).ascending():Sort.by(sortField).descending()
        );
    }

    private void deleteFiles(SubscriptionRequest subscriptionRequest) {
        deleteFile(DeleteFileRequest.builder().key(subscriptionRequest.getAccreditationKey()).build());
        deleteFile(DeleteFileRequest.builder().key(subscriptionRequest.getUniversityLogoKey()).build());
    }

    private AfterUpdateResponse handleUpdateRequest(SubscriptionRequest subscriptionRequest, UpdateSubscriptionRequestDto updateSubscriptionRequestDto){
        if(updateSubscriptionRequestDto.getStatus().equalsIgnoreCase("APPROVED")){
            if(subscriptionRequest.getStatus().equals(Status.APPROVED)){
                return AfterUpdateResponse
                        .builder()
                        .subscriptionRequestResponseDto(subscriptionMapper.toDto(subscriptionRequest))
                        .warn("Subscription request already approved")
                        .build();
            }
            AfterUpdateResponse response=createUniversity(subscriptionRequest);
            if(response.getUniversityResponse()==null){
                subscriptionRequest.setStatus(Status.PENDING);
                subscriptionRequestRepository.save(subscriptionRequest);
                response.setWarn("Service might not be available right now or there maybe a university exists with the same data, subscription request will be pending until university is created.");
                return response;
            }
            subscriptionRequest.setStatus(Status.APPROVED);
            subscriptionRequestRepository.save(subscriptionRequest);
            return response;

        }else if(updateSubscriptionRequestDto.getStatus().equalsIgnoreCase("REJECTED")){
            if(subscriptionRequest.getStatus().equals(Status.APPROVED)){
                return AfterUpdateResponse
                        .builder()
                        .subscriptionRequestResponseDto(subscriptionMapper.toDto(subscriptionRequest))
                        .warn("Subscription request already approved")
                        .build();
            }
            subscriptionRequest.setStatus(Status.REJECTED);
            subscriptionRequestRepository.save(subscriptionRequest);
            return AfterUpdateResponse
                    .builder()
                    .subscriptionRequestResponseDto(subscriptionMapper.toDto(subscriptionRequest))
                    .build();
        }else{
            return AfterUpdateResponse
                    .builder()
                    .subscriptionRequestResponseDto(subscriptionMapper.toDto(subscriptionRequest))
                    .build();
        }
    }
    private AfterUpdateResponse createUniversity(SubscriptionRequest subscriptionRequest){
        ResponseEntity<UniversityResponse> universityResponseResponseEntity=universityFeignClient
                .createUniversity(subscriptionMapper.toUniversityRequestDto(subscriptionRequest));
        return AfterUpdateResponse.builder()
                .subscriptionRequestResponseDto(subscriptionMapper.toDto(subscriptionRequest))
                .universityResponse(universityResponseResponseEntity.getBody())
                .build();
    }

    private  boolean isInvalidValidContentType(MultipartFile file) {
        String contentType = file.getContentType();
        return !ALLOWED_CONTENT_TYPES.contains(contentType);
    }

}
