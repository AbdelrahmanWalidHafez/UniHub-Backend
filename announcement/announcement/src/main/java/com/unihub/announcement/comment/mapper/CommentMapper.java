package com.unihub.announcement.comment.mapper;

import com.unihub.announcement.comment.dto.request.CreateCommentRequest;
import com.unihub.announcement.comment.dto.response.CommentDto;
import com.unihub.announcement.comment.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CreateCommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        return comment;
    }

    public CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCid(comment.getCid());
        commentDto.setEdited(comment.isEdited());
        commentDto.setRepliesCount(comment.getRepliesCounts());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setCreatedBy(comment.getCreatedBy());
        commentDto.setUpdatedAt(comment.getUpdatedAt());
        commentDto.setUpdatedBy(comment.getUpdatedBy());
        return commentDto;
    }
}
