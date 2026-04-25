package com.unihub.classroom.assginement.repository;

import com.unihub.classroom.assginement.model.Submission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    @EntityGraph(attributePaths = {"submissionUrls"})
    Optional<Submission> findBySidAndCreatedBy(UUID sid,String email);

    @EntityGraph(attributePaths = {"submissionUrls"})
    Optional<Submission> findByCreatedByAndAssignment_Id(String email,UUID assignmentId);


    @EntityGraph(attributePaths = {"submissionUrls"})
    @Query("SELECT s FROM Submission s WHERE s.sid = :sid AND s.createdBy = :email")
    Optional<Submission> findByIdForDeletion(@Param("sid") UUID sid, @Param("email") String email);

    @Query("""
    SELECT DISTINCT s
    FROM Submission s
    JOIN FETCH s.assignment a
    JOIN FETCH a.material m
    JOIN FETCH m.classroom c
    LEFT JOIN FETCH s.submissionUrls
    WHERE a.id = :assignmentId
    AND (
        c.createdBy = :email
        OR a.createdBy = :email
        OR m.createdBy = :email
    )
""")
    List<Submission> findAllByAssignment(
            UUID assignmentId,
            String email,
            Pageable pageable
    );

    @Query("""
    SELECT s
    FROM Submission s
    JOIN FETCH s.assignment a
    WHERE s.sid = :sid
    AND (
        a.createdBy = :email
        OR a.material.createdBy = :email
        OR a.material.classroom.createdBy = :email
    )
""")
    Optional<Submission> findSubmission(
            @Param("sid") UUID sid,
            @Param("email") String email
    );

}
