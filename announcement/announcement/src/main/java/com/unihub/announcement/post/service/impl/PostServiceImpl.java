package com.unihub.announcement.post.service.impl;

import com.unihub.announcement.like.repository.PostLikeRepository;
import com.unihub.announcement.post.dto.request.CreatePostRequest;
import com.unihub.announcement.post.dto.request.DeleteFileRequest;
import com.unihub.announcement.post.dto.request.UploadFileRequest;
import com.unihub.announcement.post.dto.response.PostDto;
import com.unihub.announcement.post.mapper.PostMapper;
import com.unihub.announcement.post.model.Post;
import com.unihub.announcement.post.model.Status;
import com.unihub.announcement.post.repository.PostRepository;
import com.unihub.announcement.post.service.IPostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements IPostService {

    private final PostMapper postMapper;

    private final StreamBridge streamBridge;

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;
    @Value("${aws.bucket}")
    private String bucketLink;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "video/mp4",
            "video/quicktime",
            "video/x-msvideo",
            "video/webm",
            "video/x-matroska"
    );

    @Override
    public PostDto createPost(CreatePostRequest postRequest, MultipartFile media, HttpServletRequest request) throws IOException {
        Post post =generateEntity(postRequest,request);
        if (media!=null) {
            uploadFile(post, media);
        }
        post=postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public PostDto getPost(UUID id, HttpServletRequest request) {
        return  postMapper.toDto(fetchPost(id ,fetchCidFromHeader(request)),fetchEmailFromHeader(request));
    }

    @Override
    public List<PostDto> getPosts(HttpServletRequest request,int pageNum, String sortDir, String sortField) {
        Pageable pageable=createPageable(pageNum,sortDir,sortField);
        return postsDto(postRepository.findAllByCidAndStatus(fetchCidFromHeader(request),Status.ACCEPTED,pageable),request);
    }

    @Override
    public List<PostDto> getUserPosts(HttpServletRequest request,int pageNum, String sortDir, String sortField) {
        Pageable pageable=createPageable(pageNum,sortDir,sortField);
        return postsDto(postRepository.findAllByCidAndCreatedBy(fetchCidFromHeader(request),fetchEmailFromHeader(request),pageable),request);
    }


    @Override
    public PostDto publish(UUID id,HttpServletRequest request) {
        Post post = fetchPost(id, fetchCidFromHeader(request),fetchEmailFromHeader(request));
        post.setStatus(Status.PENDING);
        postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    @Transactional
    public PostDto editPost(UUID id,
                            CreatePostRequest request,
                            MultipartFile media,
                            boolean removeMedia,
                            HttpServletRequest httpRequest) throws IOException {
        Post post = fetchPost(id, fetchCidFromHeader(httpRequest), fetchEmailFromHeader(httpRequest));
        updateStatus(post);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        handleMediaUpdate(post, media, removeMedia);
        postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public List<PostDto> getPosts(HttpServletRequest request, int pageNum, String sortDir, String sortField, Status status) {
        Pageable pageable=createPageable(pageNum,sortDir,sortField);
        if (status!=null) {
            return postsDto(postRepository.findAllByCidAndStatus(fetchCidFromHeader(request),status,pageable),request);
        }
        return postsDto(postRepository.findAllByCid(fetchCidFromHeader(request),pageable),request);
    }

    @Override
    @Transactional
    public PostDto updateStatus(UUID id, HttpServletRequest request,Status status) {
        Post post = fetchPost(id, fetchCidFromHeader(request));
        post.setStatus(status);
        postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    @Transactional
    public void deletePost(UUID id, HttpServletRequest request) {
        Post post = fetchPost(id, fetchCidFromHeader(request),fetchEmailFromHeader(request));
        deleteFile(post.getMediaUrl());
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public void delete(UUID id, HttpServletRequest request) {
        Post post = fetchPost(id, fetchCidFromHeader(request));
        postRepository.delete(post);
    }

    public Post fetchPost(UUID id,UUID cid){
        return  postRepository
                .findByPidAndCid(id,cid)
                .orElseThrow(()->new EntityNotFoundException("Resource Not found"));
    }

    public UUID fetchCidFromHeader(HttpServletRequest request){
        return UUID.fromString(request.getHeader("X-User-College-Id"));
    }

    public String fetchEmailFromHeader(HttpServletRequest request){
        return request.getHeader("X-User-Email");
    }

    private Post generateEntity(CreatePostRequest createPostRequest,HttpServletRequest request) {
        Post post = postMapper.toEntity(createPostRequest);
        post.setStatus(Status.DRAFT);
        post.setLikesCount(0L);
        post.setCommentsCount(0L);
        post.setCid(fetchCidFromHeader(request));
        return post;
    }

    private String generateFileKey(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    private void uploadFile(Post post, MultipartFile media) throws IOException {
        if(isInvalidValidContentType(media)){
            throw new IllegalArgumentException("Invalid content type");
        }
        post.setMediaUrl(bucketLink+generateFileKey());
        UploadFileRequest uploadFileRequest=UploadFileRequest.builder()
                .fileContent(Base64.getEncoder().encodeToString(media.getBytes()))
                .key(post.getMediaUrl().substring(post.getMediaUrl().lastIndexOf("/")+1))
                .contentType(media.getContentType())
                .build();
        uploadFile(uploadFileRequest);
    }

    private  boolean isInvalidValidContentType(MultipartFile file) {
        String contentType = file.getContentType();
        return !ALLOWED_CONTENT_TYPES.contains(contentType);
    }

    private void uploadFile(UploadFileRequest uploadFileRequest){
        streamBridge.send("uploadFile-out-0",uploadFileRequest);
    }

    private Post fetchPost(UUID id,UUID cid,String email){
        return postRepository
                .findByPidAndCidAndCreatedBy(id,cid,email)
                .orElseThrow(()->new EntityNotFoundException("Resource Not found"));
    }

    private void updateStatus(Post post){
        if(post.getStatus() == Status.ACCEPTED || post.getStatus() == Status.REJECTED){
            post.setStatus(Status.PENDING);
        }
    }

    private void handleMediaUpdate(Post post,
                                   MultipartFile media,
                                   boolean removeMedia) throws IOException {

        if(removeMedia && post.getMediaUrl() != null){
            deleteFile(post.getMediaUrl());
            post.setMediaUrl(null);
            return;
        }
        if(media != null && !media.isEmpty()){
            if(post.getMediaUrl() != null){
                deleteFile(post.getMediaUrl());
            }
            uploadFile(post, media);
        }
    }

    private void deleteFile(String key) {
        if (key != null) {
            streamBridge.send("deleteFile-out-0", DeleteFileRequest.builder().key(key).build());
        }
    }

    private Pageable createPageable(int pageNum, String sortDir, String sortField){
        int pageSize=5;
        return  PageRequest.of(
                pageNum-1,
                pageSize,
                sortDir.equalsIgnoreCase("asc")? Sort.by(sortField).ascending():Sort.by(sortField).descending()
        );
    }
    private List<PostDto> postsDto(List<Post> posts, HttpServletRequest request){

        List<UUID> postIds = posts.stream().map(Post::getPid).toList();
        Set<UUID> likedPostIds = postLikeRepository
                .findAllByPostIdsAndCreatedBy(postIds,fetchEmailFromHeader(request))
                .stream()
                .map(like -> like.getPost().getPid())
                .collect(Collectors.toSet());
        return posts.stream().map(post -> {
            PostDto dto = postMapper.toDto(post);
            dto.setLikedByCurrentUser(likedPostIds.contains(post.getPid()));
            return dto;
        }).toList();
    }
}
