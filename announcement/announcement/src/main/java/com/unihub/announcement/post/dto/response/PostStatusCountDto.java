package com.unihub.announcement.post.dto.response;

import com.unihub.announcement.post.model.Status;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostStatusCountDto {

    private Status status;

    private Long count;
}
