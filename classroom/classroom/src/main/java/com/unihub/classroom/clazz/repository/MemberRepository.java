package com.unihub.classroom.clazz.repository;

import com.unihub.classroom.clazz.model.ClassRoom;
import com.unihub.classroom.clazz.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository <Member, UUID>{

    Optional<Member> findByEmailAndClassroom_Id(String email, UUID classroomId);

    @Query("""
    SELECT m.classroom
    FROM Member m
    JOIN m.classroom c
    WHERE m.email = :email
      AND c.archived = false
""")
    List<ClassRoom> findActiveClassRoomsByEmail(@Param("email") String email);

    @Query("""
    SELECT m.classroom
    FROM Member m
    JOIN m.classroom c
    WHERE m.email = :email
      AND c.archived = true
""")
    List<ClassRoom> findArchivedClassRoomsByEmail(@Param("email") String email);

    @Query("""
    SELECT m
    FROM Member m
    JOIN m.classroom c
    WHERE c.id = :classId
    AND (
        c.createdBy = :email
        OR EXISTS (
            SELECT 1
            FROM Member m2
            WHERE m2.classroom.id = :classId
            AND m2.email = :email
        )
    )
""")
    Page<Member> findMembersByClassroomId(
            @Param("classId") UUID classId,
            @Param("email") String email,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT c.id
    FROM ClassRoom c
    LEFT JOIN Member m ON m.classroom.id = c.id
    WHERE  c.createdBy = :email OR m.email = :email
""")
    List<UUID> findClassRoomsForUser(@Param("email") String email);
}
