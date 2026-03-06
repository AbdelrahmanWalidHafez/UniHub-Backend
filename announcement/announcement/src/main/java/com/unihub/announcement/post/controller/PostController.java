package com.unihub.announcement.post.controller;

import com.unihub.announcement.post.dto.request.CreatePostRequest;
import com.unihub.announcement.post.dto.response.PostDto;
import com.unihub.announcement.post.dto.response.PostsDto;
import com.unihub.announcement.post.model.Status;
import com.unihub.announcement.post.service.IPostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final IPostService postService;

    @PostMapping(value = "/public/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> create(@RequestPart(value = "data") @Valid CreatePostRequest createPostRequest,
                                          @RequestPart(value = "media",required = false) MultipartFile media,
                                          HttpServletRequest request) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(createPostRequest,media,request));
    }

    @GetMapping("/public/get-post/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id,HttpServletRequest request){
        return ResponseEntity.ok(postService.getPost(id,request));
    }

    @GetMapping("/public/get-posts")
    public ResponseEntity<PostsDto> getPosts(HttpServletRequest request,
                                         @RequestParam(name = "page_num", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "sort_dir", defaultValue = "desc") String sortDir,
                                         @RequestParam(value = "sort_field", defaultValue = "createdAt") String sortField){
        return ResponseEntity.ok(
                PostsDto.builder()
                        .posts(postService.getPosts(request,pageNum,sortDir,sortField))
                        .build()
        );
    }

    @GetMapping("/public/get-my-posts")
    public ResponseEntity<PostsDto> getMyPosts(HttpServletRequest request,
                                         @RequestParam(name = "page_num", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "sort_dir", defaultValue = "desc") String sortDir,
                                         @RequestParam(value = "sort_field", defaultValue = "createdAt") String sortField){
        return ResponseEntity.ok(
                PostsDto.builder()
                        .posts(postService.getUserPosts(request,pageNum,sortDir,sortField))
                        .build()
        );
    }

    @PatchMapping("/public/publish/{id}")
    public ResponseEntity<PostDto>publish(@PathVariable UUID id,HttpServletRequest request){
        return ResponseEntity.ok(postService.publish(id,request));
    }

    @PutMapping(value = "/public/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> editPost(
            @PathVariable UUID id,
            @RequestPart("data") @Valid CreatePostRequest request,
            @RequestPart(value = "media", required = false) MultipartFile media,
            @RequestParam(value = "remove_media", defaultValue = "false") boolean removeMedia,
            HttpServletRequest httpRequest) throws IOException {

        return ResponseEntity.ok(
                postService.editPost(id, request, media, removeMedia, httpRequest)
        );
    }

    @DeleteMapping("/public/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,HttpServletRequest request){
        postService.deletePost(id,request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/secretary/get-posts")
    public ResponseEntity<PostsDto> getPosts(HttpServletRequest request,
                                             @RequestParam(name = "page_num", defaultValue = "1") int pageNum,
                                             @RequestParam(value = "sort_dir", defaultValue = "desc") String sortDir,
                                             @RequestParam(value = "sort_field", defaultValue = "createdAt") String sortField,
                                             @RequestParam(value="status",required = false) Status status){
        return ResponseEntity.ok(
                PostsDto.builder()
                        .posts(postService.getPosts(request,pageNum,sortDir,sortField,status))
                        .build()
        );
    }

    @PatchMapping("/secretary/accept/post/{id}")
    public ResponseEntity<PostDto> accept(@PathVariable UUID id,HttpServletRequest request){
        return ResponseEntity.ok(postService.updateStatus(id,request,Status.ACCEPTED));
    }

    @PatchMapping("/secretary/reject/post/{id}")
    public ResponseEntity<PostDto> reject(@PathVariable UUID id,HttpServletRequest request){
        return ResponseEntity.ok(postService.updateStatus(id,request,Status.REJECTED));
    }

    @DeleteMapping("/secretary/delete/post/{id}")
    public ResponseEntity<PostDto> deletePost(@PathVariable UUID id,HttpServletRequest request){
        postService.delete(id,request);
        return ResponseEntity.noContent().build();
    }

}
