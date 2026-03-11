package com.unihub.subscription.subscriptionrequest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UploadFileRequest {

    private byte[] fileContent;

    private String key;

    private String contentType;
}
