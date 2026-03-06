package com.unihub.announcement.like.service.impl;

import com.unihub.announcement.like.model.PostLike;
import com.unihub.announcement.like.repository.PostLikeRepository;
import com.unihub.announcement.like.service.ILikeService;
import com.unihub.announcement.post.model.Post;
import com.unihub.announcement.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements ILikeService {

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void toggleLike(UUID id, HttpServletRequest request) {
        Post post = fetchPost(id, fetchCidFromHeader(request));
        String email = fetchEmailFromHeader(request);
        postLikeRepository.findByPostAndCreatedBy(post, email)
                .ifPresentOrElse(
                        like -> {
                            post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
                            postLikeRepository.delete(like);
                        }, () -> {
                            PostLike like = PostLike.builder().post(post).build();
                            postLikeRepository.save(like);
                            post.setLikesCount(post.getLikesCount() + 1);
                        }
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getUsersWhoLiked(UUID postId, HttpServletRequest request, int pageNum) {
        Post post = fetchPost(postId, fetchCidFromHeader(request));
        Pageable pageable = PageRequest.of(pageNum, 5, Sort.by("createdAt").descending());
        return postLikeRepository.findAllByPost(post, pageable)
                .stream()
                .map(PostLike::getCreatedBy)
                .toList();
    }

    private Post fetchPost(UUID id, UUID cid){
        return  postRepository
                .findByPidAndCid(id,cid)
                .orElseThrow(()->new EntityNotFoundException("Resource Not found"));
    }


    private UUID fetchCidFromHeader(HttpServletRequest request){
        return UUID.fromString(request.getHeader("X-User-College-Id"));
    }

    private String fetchEmailFromHeader(HttpServletRequest request){
        return request.getHeader("X-User-Email");
    }

}
