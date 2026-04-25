package com.unihub.classroom.comment.service;

import com.unihub.classroom.comment.dto.request.CreateCommentRequest;
import com.unihub.classroom.comment.dto.response.CommentDto;
import com.unihub.classroom.comment.dto.response.CommentsDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface ICommentService {

    CommentDto createCommentOnMaterial(UUID materialId, CreateCommentRequest createCommentRequest, HttpServletRequest request);

    void deleteComment(UUID cid, HttpServletRequest request);

    CommentDto editComment(UUID cid, CreateCommentRequest createCommentRequest, HttpServletRequest request);

    CommentsDto getComments(UUID mid, HttpServletRequest request, int pageNum);
}
