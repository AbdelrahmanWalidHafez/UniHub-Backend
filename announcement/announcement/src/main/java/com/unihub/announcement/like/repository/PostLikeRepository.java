package com.unihub.announcement.like.repository;

import com.unihub.announcement.like.model.PostLike;
import com.unihub.announcement.post.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {

    boolean existsByPostAndCreatedBy(Post post, String email);

    Optional<PostLike> findByPostAndCreatedBy(Post post, String createdBy);

    List<PostLike> findAllByPost(Post post, Pageable pageable);

    @Query("SELECT pl.post.pid FROM PostLike pl WHERE pl.createdBy = :email AND pl.post.cid = :cid")
    Set<UUID> findPostIdsLikedByUser(@Param("email") String email, @Param("cid") UUID cid);

    List<PostLike> findAllByPost_PidAndCreatedBy(List<UUID> postIds, String currentUser);
}
