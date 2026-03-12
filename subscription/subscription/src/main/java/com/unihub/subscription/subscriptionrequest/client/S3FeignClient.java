package com.unihub.subscription.subscriptionrequest.client;

import com.unihub.subscription.subscriptionrequest.client.fallback.S3FallBack;
import com.unihub.subscription.subscriptionrequest.dto.request.UploadFileRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "s3", fallback = S3FallBack.class)
public interface S3FeignClient {

    @PostMapping(value = "/api/v1/upload-file", consumes = "application/json")
    ResponseEntity<Void> uploadFile(@RequestBody UploadFileRequest request);
}
