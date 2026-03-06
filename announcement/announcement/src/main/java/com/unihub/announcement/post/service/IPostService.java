package com.unihub.announcement.post.service;

import com.unihub.announcement.post.dto.request.CreatePostRequest;
import com.unihub.announcement.post.dto.response.PostDto;
import com.unihub.announcement.post.model.Post;
import com.unihub.announcement.post.model.Status;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IPostService {

    PostDto createPost(CreatePostRequest postRequest, MultipartFile media, HttpServletRequest request) throws IOException;

    PostDto getPost(UUID id,HttpServletRequest request);

    List<PostDto> getPosts(HttpServletRequest request,int pageNum, String sortDir, String sortField);

    List<PostDto> getUserPosts(HttpServletRequest request,int pageNum, String sortDir, String sortField);

    PostDto publish(UUID id,HttpServletRequest request);

    PostDto editPost(UUID id, CreatePostRequest request, MultipartFile media, boolean removeMedia, HttpServletRequest httpRequest) throws IOException;

    void deletePost(UUID id,HttpServletRequest request);

    List<PostDto> getPosts(HttpServletRequest request,int pageNum, String sortDir, String sortField,Status status);

    PostDto updateStatus(UUID id, HttpServletRequest request,Status status);

    void delete(UUID id,HttpServletRequest request);

    UUID fetchCidFromHeader(HttpServletRequest request);

    String fetchEmailFromHeader(HttpServletRequest request);

    Post fetchPost(UUID id, UUID cid);
}
