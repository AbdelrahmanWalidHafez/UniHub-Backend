package com.unihub.classroom.comment.service.impl;

import com.unihub.classroom.comment.dto.request.CreateCommentRequest;
import com.unihub.classroom.comment.dto.response.CommentDto;
import com.unihub.classroom.comment.dto.response.CommentsDto;
import com.unihub.classroom.comment.mapper.CommentMapper;
import com.unihub.classroom.comment.model.Comment;
import com.unihub.classroom.comment.repository.CommentRepository;
import com.unihub.classroom.comment.service.ICommentService;
import com.unihub.classroom.material.model.Material;
import com.unihub.classroom.material.repository.MaterialRepository;
import com.unihub.classroom.utils.HttpHeadersUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {

    private final CommentMapper commentMapper;

    private final HttpHeadersUtils httpHeadersUtils;

    private final CommentRepository commentRepository;

    private final MaterialRepository materialRepository;

    @Override
    @Transactional
    public CommentDto createCommentOnMaterial(UUID materialId, CreateCommentRequest createCommentRequest, HttpServletRequest request) {
        Material material=fetchMaterial(materialId,request);
        Comment comment=commentMapper.toEntity(createCommentRequest);
        comment.setEdited(false);
        material.addComment(comment);
        material.setCommentsCount(material.getCommentsCount()+1);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(UUID cid, HttpServletRequest request) {
        Comment comment=fetchComment(cid,request);
        comment.getMaterial().setCommentsCount(Math.max(comment.getMaterial().getCommentsCount()-1,0));
        comment.getMaterial().removeComment(comment);
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public CommentDto editComment(UUID cid, CreateCommentRequest createCommentRequest, HttpServletRequest request){
        Comment comment=fetchComment(cid,request);
        comment.setContent(createCommentRequest.getContent());
        comment.setEdited(true);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentsDto getComments(UUID mid, HttpServletRequest request, int pageNum){
        return CommentsDto.builder()
                .comments(commentRepository
                        .findCommentsByMaterial(mid,httpHeadersUtils.fetchEmailFromHeader(request),generatePageable(pageNum))
                        .stream()
                        .map(commentMapper::toDto)
                        .toList()
                )
                .build();
    }


    private Material fetchMaterial(UUID materialId, HttpServletRequest request){
        return materialRepository.findMaterial(materialId, httpHeadersUtils.fetchEmailFromHeader(request))
                .orElseThrow(()->new EntityNotFoundException("Material not found with id: "+materialId));
    }

    private Comment fetchComment(UUID cid, HttpServletRequest request){
        return  commentRepository
                .findByCidAndUser(cid,httpHeadersUtils.fetchEmailFromHeader(request))
                .orElseThrow(()->new EntityNotFoundException("no comment found with id:"+cid));
    }

    private Pageable generatePageable(int pageNum){
        return PageRequest.of(pageNum-1,5, Sort.by("createdAt").descending());
    }

}
