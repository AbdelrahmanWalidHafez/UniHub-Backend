package com.unihub.classroom.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {

    @NotBlank(message = "Comment content cannot be empty")
    @Size(min=2,max = 500, message = "Comment content cannot exceed 1000 characters")
    private String content;;
}
