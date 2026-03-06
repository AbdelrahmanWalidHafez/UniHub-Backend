package com.unihub.announcement.comment.repository;

import com.unihub.announcement.comment.model.Comment;
import com.unihub.announcement.post.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    Optional<Comment> findByCidAndCreatedBy(UUID id,String email);

    List<Comment> findByPostAndParentIsNull(Post post, Pageable pageable);

    Optional<Comment> findByCidAndPost(UUID id, Post post);

    List<Comment> findByParent(Comment parent, Pageable pageable);
}
