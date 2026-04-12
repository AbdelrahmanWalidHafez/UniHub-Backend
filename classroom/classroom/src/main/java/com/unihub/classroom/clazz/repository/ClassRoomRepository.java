package com.unihub.classroom.clazz.repository;

import com.unihub.classroom.clazz.model.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, UUID> {

    Optional<ClassRoom> findByIdAndCollegeIdAndUniversityIdAndCreatedBy(UUID id, UUID collegeId, UUID universityId, String createdBy);

    Optional<ClassRoom> findByCodeAndCollegeIdAndUniversityId(String code, UUID collegeId, UUID universityId);

    Optional<ClassRoom> findByCreatedByAndId(String email, UUID id);

    List<ClassRoom> findByCreatedBy(String email);

    @Query("""
    SELECT c.createdBy
    FROM ClassRoom c
    WHERE c.id = :classId
    AND (
        c.createdBy = :email
        OR EXISTS (
            SELECT 1
            FROM Member m
            WHERE m.classroom.id = :classId
            AND m.email = :email
        )
    )
""")
    Optional<String> findOwnerEmail(
            @Param("classId") UUID classId,
            @Param("email") String email
    );
}
