package com.unihub.classroom.clazz.repository;

import com.unihub.classroom.clazz.model.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, UUID> {

    Optional<ClassRoom> findByIdAndCollegeIdAndUniversityIdAndCreatedBy(UUID id, UUID collegeId, UUID universityId, String createdBy);

    Optional<ClassRoom> findByCodeAndCollegeIdAndUniversityId(String code, UUID collegeId, UUID universityId);

    Optional<ClassRoom> findByCreatedByAndId(String email, UUID id);

    List<ClassRoom> findByCreatedBy(String email);
}
