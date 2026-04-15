package com.unihub.classroom.clazz.service.impl;

import com.unihub.classroom.clazz.dto.request.CreateClassroomDto;
import com.unihub.classroom.clazz.dto.response.ClassRoomResponse;
import com.unihub.classroom.clazz.dto.response.MemberDto;
import com.unihub.classroom.clazz.dto.response.OwnerDto;
import com.unihub.classroom.clazz.mapper.ClassRoomMapper;
import com.unihub.classroom.clazz.mapper.MemberMapper;
import com.unihub.classroom.clazz.model.ClassRoom;
import com.unihub.classroom.clazz.model.Member;
import com.unihub.classroom.clazz.repository.ClassRoomRepository;
import com.unihub.classroom.clazz.repository.MemberRepository;
import com.unihub.classroom.clazz.service.IClassRoomService;
import com.unihub.classroom.clazz.service.state.ClassRoomContext;
import com.unihub.classroom.utils.HttpHeadersUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassRoomServiceImpl  implements IClassRoomService {

    private final MemberMapper memberMapper;

    private final ClassRoomMapper classRoomMapper;

    private final MemberRepository memberRepository;

    private final ClassRoomRepository classRoomRepository;

    private final HttpHeadersUtils httpHeadersUtils;

    @Override
    public ClassRoomResponse createClassRoom(CreateClassroomDto createClassroomDto, HttpServletRequest request) {
        ClassRoom classRoom = classRoomMapper.toEntity(createClassroomDto);
        classRoom.setImageNum(getImageNum());
        classRoom.setCode(generateCode());
        classRoom.setArchived(false);
        classRoom.setUniversityId(httpHeadersUtils.fetchUidFromHeader(request));
        classRoom.setCollegeId(httpHeadersUtils.fetchCidFromHeader(request));
        classRoomRepository.save(classRoom);
        return classRoomMapper.toDto(classRoom);
    }

    @Override
    public ClassRoomResponse archiveClassRoom(UUID id,HttpServletRequest request) {
        ClassRoom classRoom = fetchClassRoom(id,request);
        ClassRoomContext classRoomContext =initContext(classRoom);
        classRoomContext.handleRequest(classRoom);
        classRoomRepository.save(classRoom);
        return classRoomMapper.toDto(classRoom);
    }

    @Override
    @Transactional
    public MemberDto joinClassRoom(String code, HttpServletRequest request) {
        String email = httpHeadersUtils.fetchEmailFromHeader(request);
        ClassRoom classRoom = fetchClassRoom(code,request);
        if (isMemberExist(email,classRoom)||isOwner(email,classRoom)){
            throw new IllegalArgumentException("Member already exists");
        }
        Member member = generateMember(email);
        classRoom.addMember(member);
        classRoomRepository.save(classRoom);
        return memberMapper.toDto(member);
    }

    @Override
    @Transactional
    public void leaveClassRoom(UUID id, HttpServletRequest request) {
        ClassRoom classRoom = fetchClassRoom(id);
        if (!isOwner(httpHeadersUtils.fetchEmailFromHeader(request), classRoom)) {
            Member member = classRoom.getMembers()
                    .stream()
                    .filter(m -> m.getEmail().equals(httpHeadersUtils.fetchEmailFromHeader(request))).findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Member not found"));
            classRoom.removeMember(member);
        } else {
            classRoomRepository.delete(classRoom);
        }
    }

    @Override
    @Transactional
    public List<MemberDto> fetchMembers(UUID id, HttpServletRequest request, int pageNum){

        return memberRepository.findMembersByClassroomId(id,httpHeadersUtils.fetchEmailFromHeader(request),generatePageable(pageNum))
                .stream()
                .map(memberMapper::toDto).toList();
    }

    @Override
    @Transactional
    public OwnerDto fetchOwner(UUID id, HttpServletRequest request) {
        return OwnerDto.builder()
                .email(
                        classRoomRepository.findOwnerEmail(id,httpHeadersUtils.fetchEmailFromHeader(request))
                        .orElseThrow(()->new EntityNotFoundException("Owner not found with id: "+id))
                )
                .build();
    }

    @Override
    public List<ClassRoomResponse> fetchEnrolledClassRooms(HttpServletRequest request) {
        return memberRepository.findActiveClassRoomsByEmail(httpHeadersUtils.fetchEmailFromHeader(request)).stream().map(classRoomMapper::toDto).toList();
    }

    @Override
    public List<ClassRoomResponse> getArchivedClassRooms(HttpServletRequest request){
        return memberRepository.findArchivedClassRoomsByEmail(httpHeadersUtils.fetchEmailFromHeader(request)).stream().map(classRoomMapper::toDto).toList();
    }

    @Override
    public List<ClassRoomResponse> fetchArchivedClassRooms(HttpServletRequest request) {
        return classRoomRepository.findByCreatedByAndArchived(httpHeadersUtils.fetchEmailFromHeader(request),true).stream().map(classRoomMapper::toDto).toList();
    }

    @Override
    public List<ClassRoomResponse> fetchMyClassRooms(HttpServletRequest request){
        return classRoomRepository.findByCreatedBy(httpHeadersUtils.fetchEmailFromHeader(request)).stream().map(classRoomMapper::toDto).toList();
    }

    @Override
    public List<ClassRoomResponse> fetchActiveClassRooms(HttpServletRequest request){
        return classRoomRepository.findByCreatedByAndArchived(httpHeadersUtils.fetchEmailFromHeader(request), false).stream().map(classRoomMapper::toDto).toList();
    }


    private int getImageNum(){
        return new Random().nextInt(10)+1;
    }

    private String generateCode() {
        return UUID.randomUUID().toString().replace("-","").substring(0, 8);
    }

    private ClassRoomContext initContext(ClassRoom classRoom){
        ClassRoomContext classRoomContext = new ClassRoomContext();
        classRoomContext.setState(classRoom);
        return classRoomContext;
    }



    private ClassRoom fetchClassRoom(UUID id,HttpServletRequest request){
        return classRoomRepository.findByIdAndCollegeIdAndUniversityIdAndCreatedBy(id
                ,httpHeadersUtils.fetchCidFromHeader(request)
                ,httpHeadersUtils.fetchUidFromHeader(request)
                ,httpHeadersUtils.fetchEmailFromHeader(request)).orElseThrow(()->new EntityNotFoundException("Classroom not found with id: "+id));
    }

    private ClassRoom fetchClassRoom(String code,HttpServletRequest request){
        return classRoomRepository.findByCodeAndCollegeIdAndUniversityId(code
                ,httpHeadersUtils.fetchCidFromHeader(request)
                ,httpHeadersUtils.fetchUidFromHeader(request)
        ).orElseThrow(()->new EntityNotFoundException("Classroom not found with code: "+code));
    }

    private ClassRoom fetchClassRoom(UUID id){
        return classRoomRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Classroom not found with id: "+id));
    }


    private boolean isMemberExist(String email,ClassRoom classRoom){
        return memberRepository.findByEmailAndClassroom_Id(email,classRoom.getId()).isPresent();
    }
    private Member generateMember(String email){
        return Member.builder()
                .email(email)
                .build();
    }

    private boolean isOwner(String email,ClassRoom classRoom){
        return classRoom.getCreatedBy().equals(email);
    }

    private Pageable generatePageable(int pageNum){
        return PageRequest.of(pageNum-1,10, Sort.by("createdAt").descending());
    }

}
