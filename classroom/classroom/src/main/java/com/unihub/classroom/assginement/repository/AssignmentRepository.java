package com.unihub.classroom.assginement.repository;

import com.unihub.classroom.assginement.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    @Query("""
    SELECT a
    FROM Assignment a
    JOIN a.material m
    JOIN m.classroom c
    WHERE a.id = :assignmentId
    AND (
        c.createdBy = :email
        OR EXISTS (
            SELECT 1
            FROM Member mem
            WHERE mem.classroom = c
            AND mem.email = :email
        )
    )
""")
    Optional<Assignment> findAssignment(
            @Param("assignmentId") UUID assignmentId,
            @Param("email") String email
    );
}
