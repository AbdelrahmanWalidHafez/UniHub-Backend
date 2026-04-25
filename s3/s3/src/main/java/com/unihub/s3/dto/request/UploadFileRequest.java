package com.unihub.s3.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UploadFileRequest {

    private byte[] fileContent;

    private String key;

    private String contentType;
}
