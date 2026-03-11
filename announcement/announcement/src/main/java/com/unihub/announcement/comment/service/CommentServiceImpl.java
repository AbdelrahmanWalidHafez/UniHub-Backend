package com.unihub.announcement.comment.service;

import com.unihub.announcement.comment.dto.request.CreateCommentRequest;
import com.unihub.announcement.comment.dto.response.CommentDto;
import com.unihub.announcement.comment.mapper.CommentMapper;
import com.unihub.announcement.comment.model.Comment;
import com.unihub.announcement.comment.repository.CommentRepository;
import com.unihub.announcement.post.model.Post;
import com.unihub.announcement.post.repository.PostRepository;
import com.unihub.announcement.post.service.IPostService;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {


    private final IPostService postService;

    private final CommentMapper commentMapper;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDto createComment(CreateCommentRequest commentRequest, UUID id, HttpServletRequest request) {
        Post post=fetchPost(id, request);
        Comment comment=commentMapper.toEntity(commentRequest);
        comment.setPost(post);
        comment.setRepliesCounts(0L);
        comment.setEdited(false);
        post.setCommentsCount(post.getCommentsCount()+1);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto createReply(UUID commentId, CreateCommentRequest commentRequest) {
        Comment commentToReply=fetchComment(commentId);
        Comment reply=commentMapper.toEntity(commentRequest);
        commentToReply.setRepliesCounts(commentToReply.getRepliesCounts()+1);
        reply.setParent(commentToReply);
        reply.setPost(commentToReply.getPost());
        reply.getPost().setCommentsCount(reply.getPost().getCommentsCount()+1);
        return commentMapper.toDto(commentRepository.save(reply));
    }

    @Override
    @Transactional
    public CommentDto updateComment(UUID commentId,CreateCommentRequest commentRequest,HttpServletRequest request){
        Comment comment=fetchComment(commentId, postService.fetchEmailFromHeader(request));
        comment.setContent(commentRequest.getContent());
        comment.setEdited(true);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getComments(UUID postId,int pageNum,HttpServletRequest request) {
        Post post=fetchPost(postId, request);
        return  commentRepository.findByPostAndParentIsNull(post,createPageable(pageNum)).stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getReplies(UUID commentId, int pageNum) {
        return  commentRepository.findByParent(fetchComment(commentId),createPageable(pageNum)).stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(UUID commentId,HttpServletRequest request) {
        Comment comment=fetchComment(commentId, postService.fetchEmailFromHeader(request));
        comment.getPost().setCommentsCount(Math.max(0,comment.getPost().getCommentsCount()-1-comment.getRepliesCounts()));
        if(comment.getParent()!=null){
            comment.getParent().setRepliesCounts(Math.max(0,comment.getParent().getRepliesCounts()-1));
        }
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void deleteCommentSecretary(UUID postId,UUID commentId,HttpServletRequest request) {
        Post post=fetchPost(postId, request);
        Comment comment=fetchComment(commentId,post);
        comment.getPost().setCommentsCount(Math.max(0,comment.getPost().getCommentsCount()-1+comment.getRepliesCounts()));
        if(comment.getParent()!=null){
            comment.getParent().setRepliesCounts(Math.max(0,comment.getParent().getRepliesCounts()-1));
        }
        commentRepository.delete(comment);
    }

    private Post fetchPost(UUID id, HttpServletRequest request) {
        return postService.fetchPost(id,postService.fetchCidFromHeader(request));
    }

    private Comment fetchComment(UUID id){
        return commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private Comment fetchComment(UUID id,String email){
        return commentRepository.findByCidAndCreatedBy(id,email).orElseThrow(EntityNotFoundException::new);
    }

    private Comment fetchComment(UUID id,Post post){
        return commentRepository.findByCidAndPost(id,post).orElseThrow(EntityNotFoundException::new);
    }

    private Pageable createPageable(int pageNum){
        return PageRequest.of(pageNum-1, 5, Sort.by("createdAt").descending());
    }

}
