package com.unihub.announcement.post.repository;

import com.unihub.announcement.post.dto.response.PostStatusCountDto;
import com.unihub.announcement.post.model.Post;
import com.unihub.announcement.post.model.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findAllByCidAndStatus(UUID id, Status status, Pageable pageable);

    List<Post> findAllByCidAndCreatedBy(UUID id, String email, Pageable pageable);

    Optional<Post> findByPidAndCidAndCreatedBy(UUID id, UUID cid, String createdBy);

    Optional<Post> findByPidAndCid(UUID id, UUID cid);

    List<Post> findAllByCid(UUID cid,Pageable pageable);

    @Query("""
       SELECT new com.unihub.announcement.post.dto.response.PostStatusCountDto(p.status, COUNT(p))
       FROM Post p
       WHERE p.createdBy = :email
       GROUP BY p.status
       """)
    List<PostStatusCountDto> countPostsByStatusForUser(@Param("email") String email);
}
