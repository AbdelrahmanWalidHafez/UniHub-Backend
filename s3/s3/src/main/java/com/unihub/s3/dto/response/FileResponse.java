package com.unihub.s3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class FileResponse {

    private final byte[] content;

    private final String contentType;

    public byte[] getContent() { return content; }

    public String getContentType() { return contentType; }
}
