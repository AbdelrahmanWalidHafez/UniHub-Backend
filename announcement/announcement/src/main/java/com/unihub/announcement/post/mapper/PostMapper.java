package com.unihub.announcement.post.mapper;

import com.unihub.announcement.post.dto.request.CreatePostRequest;
import com.unihub.announcement.post.dto.response.PostDto;
import com.unihub.announcement.post.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {


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
        postDto.setMedia(post.getMedia());
        postDto.setCid(post.getCid());
        postDto.setUpdatedAt(post.getUpdatedAt());
        postDto.setUpdatedBy(post.getUpdatedBy());
        postDto.setCreatedBy(post.getCreatedBy());
        postDto.setCreatedAt(post.getCreatedAt());
        return postDto;
    }
}
