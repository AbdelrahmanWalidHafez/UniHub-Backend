package com.unihub.announcement.comment.dto.response;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDto {

    List<CommentDto> comments;
}
