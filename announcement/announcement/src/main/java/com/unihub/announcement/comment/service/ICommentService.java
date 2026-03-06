package com.unihub.announcement.comment.service;

import com.unihub.announcement.comment.dto.request.CreateCommentRequest;
import com.unihub.announcement.comment.dto.response.CommentDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface ICommentService {

    CommentDto createComment(CreateCommentRequest commentRequest, UUID id, HttpServletRequest request);

    CommentDto createReply(UUID commentId, CreateCommentRequest commentRequest);

    CommentDto updateComment(UUID commentId,CreateCommentRequest commentRequest,HttpServletRequest request);

    CommentDto getComment(UUID commentId);

    List<CommentDto> getComments(UUID postId, int pageNum, HttpServletRequest request);

    List<CommentDto> getReplies(UUID commentId, int pageNum);

    void deleteComment(UUID commentId,HttpServletRequest request);

    void deleteCommentSecretary(UUID postId,UUID commentId,HttpServletRequest request);
}
