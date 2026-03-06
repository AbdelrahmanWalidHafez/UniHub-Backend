package com.unihub.announcement.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {


    @NotBlank(message = "Title is required")
    @Pattern(regexp = "^[^<>]*$", message = "Title contains invalid characters")
    @Size(min = 3, max = 70, message = "Title must be between 3 and 70 characters")

    private String title;

    @Size(min = 10, max = 10000, message = "Content must be between 10 and 10000 characters")
    private String content;
}
