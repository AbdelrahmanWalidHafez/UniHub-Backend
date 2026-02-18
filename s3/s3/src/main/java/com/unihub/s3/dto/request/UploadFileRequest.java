package com.unihub.s3.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UploadFileRequest {

    private String fileContent;

    private String key;

    private String contentType;
}
