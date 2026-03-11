package com.unihub.announcement.post.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostsDto {

    List<PostDto> posts;
}
