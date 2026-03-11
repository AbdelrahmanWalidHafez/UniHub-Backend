package com.unihub.announcement.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UploadFileRequest {

    private String fileContent;

    private String key;

    private String contentType;
}