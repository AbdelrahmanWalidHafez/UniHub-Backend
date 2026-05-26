package com.unihub.chat.client.fallback;

import com.unihub.chat.client.S3FeignClient;
import com.unihub.chat.client.dto.UploadFileRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class S3FallBack implements S3FeignClient {

    @Override
    public ResponseEntity<Void> uploadFile(UploadFileRequest request) {
        log.error("Feign fallback: S3 service unavailable. Key: {}", request.getKey());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
