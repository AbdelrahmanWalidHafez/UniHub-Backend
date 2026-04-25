package com.unihub.classroom.comment.mapper;

import com.unihub.classroom.comment.dto.request.CreateCommentRequest;
import com.unihub.classroom.comment.dto.response.CommentDto;
import com.unihub.classroom.comment.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CreateCommentRequest request){
        Comment comment=new Comment();
        comment.setContent(request.getContent());
        return comment;
    }

    public CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCid(comment.getCid());
        commentDto.setEdited(comment.isEdited());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setCreatedBy(comment.getCreatedBy());
        commentDto.setUpdatedAt(comment.getUpdatedAt());
        commentDto.setUpdatedBy(comment.getUpdatedBy());
        return commentDto;
    }

}
