package com.unihub.announcement.post.mapper;

import com.unihub.announcement.like.repository.PostLikeRepository;
import com.unihub.announcement.post.dto.request.CreatePostRequest;
import com.unihub.announcement.post.dto.response.PostDto;
import com.unihub.announcement.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final PostLikeRepository postLikeRepository;

    public Post toEntity(CreatePostRequest createPostRequest) {
        Post post = new Post();
        post.setTitle(createPostRequest.getTitle());
        post.setContent(createPostRequest.getContent());
        return post;
    }

    public PostDto toDto(Post post){
        PostDto postDto = new PostDto();
        postDto.setId(post.getPid());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setStatus(post.getStatus());
        postDto.setMediaUrl(post.getMediaUrl());
        postDto.setCid(post.getCid());
        postDto.setLikesCount(post.getLikesCount());
        postDto.setCommentsCount(post.getCommentsCount());
        postDto.setUpdatedAt(post.getUpdatedAt());
        postDto.setUpdatedBy(post.getUpdatedBy());
        postDto.setCreatedBy(post.getCreatedBy());
        postDto.setCreatedAt(post.getCreatedAt());
        return postDto;
    }

    public PostDto toDto(Post post,String currentUser){
        PostDto postDto = toDto(post);
        postDto.setLikedByCurrentUser(postLikeRepository.existsByPostAndCreatedBy(post,currentUser));
        return postDto;
    }

}
