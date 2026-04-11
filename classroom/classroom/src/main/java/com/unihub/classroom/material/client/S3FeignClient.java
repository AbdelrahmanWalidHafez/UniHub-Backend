package com.unihub.classroom.material.client;

import com.unihub.classroom.material.client.fallback.S3FallBack;
import com.unihub.classroom.material.dto.UploadFileRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "s3", fallback = S3FallBack.class)
public interface S3FeignClient {

    @PostMapping(value = "/api/v1/upload-file", consumes = "application/json")
    ResponseEntity<Void> uploadFile(@RequestBody UploadFileRequest request);
}
