package com.unihub.classroom.assginement.repository;

import com.unihub.classroom.assginement.model.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    Optional<Submission> findBySidAndCreatedBy(UUID sid,String email);

    @Query("""
    SELECT s
    FROM Submission s
    JOIN s.assignment a
    JOIN a.material m
    JOIN m.classroom c
    WHERE s.sid = :submissionId
    AND (
        c.createdBy = :email
        OR s.createdBy = :email
        OR a.createdBy= :email
    )
""")
    Optional<Submission> findSubmission(
            @Param("submissionId") UUID submissionId,
            @Param("email") String email
    );

    @Query("""
    SELECT s
    FROM Submission s
    JOIN FETCH s.assignment a
    JOIN FETCH a.material m
    JOIN FETCH m.classroom c
    WHERE a.id = :assignmentId
    AND c.createdBy = :email
""")
    Page<Submission> findAllByAssignment(
            @Param("assignmentId") UUID assignmentId,
            @Param("email") String email, Pageable pageable
    );

    Optional<Submission> findBySidAndCreatedByAndAssignment_Id(UUID sid,String email,UUID assignmentId);
}
