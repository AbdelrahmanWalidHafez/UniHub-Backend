package com.unihub.announcement.comment.controller;

import com.unihub.announcement.comment.dto.request.CreateCommentRequest;
import com.unihub.announcement.comment.dto.response.CommentDto;
import com.unihub.announcement.comment.dto.response.CommentsDto;
import com.unihub.announcement.comment.service.ICommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final ICommentService commentService;


    @PostMapping("/public/create/{postId}")
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest,@PathVariable UUID postId, HttpServletRequest request) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(createCommentRequest,postId,request));
    }

    @PostMapping("/public/reply/{commentId}")
    public ResponseEntity<CommentDto>replyToComment(@Valid @RequestBody CreateCommentRequest createCommentRequest, @PathVariable UUID commentId) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(commentService.createReply(commentId,createCommentRequest));
    }

    @PatchMapping("/public/update/{commentId}")
    public ResponseEntity<CommentDto>updateComment(@Valid @RequestBody CreateCommentRequest createCommentRequest, @PathVariable UUID commentId, HttpServletRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId,createCommentRequest,request));
    }

    @GetMapping("/public/get-comment/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable UUID commentId) {
        return ResponseEntity.ok(commentService.getComment(commentId));
    }

    @GetMapping("/public/get-comments/{postId}")
    public ResponseEntity<CommentsDto> getComments(@PathVariable UUID postId,@RequestParam(value = "page_num",defaultValue = "1") int pageNum,HttpServletRequest request) {
        return ResponseEntity.ok(CommentsDto.builder().comments(commentService.getComments(postId,pageNum,request)).build());
    }

    @GetMapping("/public/get-comments-reply/{commentId}")
    public ResponseEntity<CommentsDto> getCommentReplies(@PathVariable UUID commentId,@RequestParam(value = "page_num",defaultValue = "1") int pageNum) {
        return ResponseEntity.ok(CommentsDto.builder().comments(commentService.getReplies(commentId,pageNum)).build());
    }

    @DeleteMapping("/public/delete/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId,HttpServletRequest request) {
        commentService.deleteComment(commentId,request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/secretary/delete/{postId}/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID postId,@PathVariable UUID commentId,HttpServletRequest request) {
        commentService.deleteCommentSecretary(postId,commentId,request);
        return ResponseEntity.noContent().build();
    }

}
