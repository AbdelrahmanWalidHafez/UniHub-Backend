package com.unihub.classroom.comment.controller;

import com.unihub.classroom.comment.dto.request.CreateCommentRequest;
import com.unihub.classroom.comment.dto.response.CommentDto;
import com.unihub.classroom.comment.dto.response.CommentsDto;
import com.unihub.classroom.comment.service.ICommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.PUT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final ICommentService commentService;

    @PostMapping("/create-material/{id}")
    public ResponseEntity<CommentDto> createComment(@PathVariable UUID id, @Valid @RequestBody CreateCommentRequest createCommentRequest, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createCommentOnMaterial(id,createCommentRequest,request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable UUID id, HttpServletRequest request){
        commentService.deleteComment(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<CommentDto> editComment(@PathVariable UUID id, @Valid @RequestBody CreateCommentRequest createCommentRequest, HttpServletRequest request){
        return ResponseEntity.ok(commentService.editComment(id,createCommentRequest,request));
    }

    @GetMapping("/get-material-comments/{id}")
    public ResponseEntity<CommentsDto> getComment(@PathVariable UUID id, HttpServletRequest request, @RequestParam(value = "page_num",defaultValue = "1")int pageNum){
        return ResponseEntity.ok(commentService.getComments(id,request,pageNum));
    }
}
