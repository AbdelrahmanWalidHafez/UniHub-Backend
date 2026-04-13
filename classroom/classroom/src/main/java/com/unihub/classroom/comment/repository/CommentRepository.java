package com.unihub.classroom.comment.repository;

import com.unihub.classroom.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    Optional<Comment> findByCidAndCreatedBy(UUID cid,String email);


    @Query("""
        SELECT cmt
        FROM Comment cmt
        JOIN cmt.material m
        JOIN m.classroom c
        WHERE m.mid = :materialId
        AND (
            c.createdBy = :email
            OR EXISTS (
                SELECT 1
                FROM Member mem
                WHERE mem.classroom.id = c.id
                AND mem.email = :email
            )
        )
        ORDER BY cmt.createdAt DESC
    """)
    Page<Comment> findCommentsByMaterial(
            @Param("materialId") UUID materialId,
            @Param("email") String email,
            Pageable pageable
    );
}

