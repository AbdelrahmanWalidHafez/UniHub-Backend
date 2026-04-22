package com.unihub.subscription.subscriptionrequest.client.fallback;

import com.unihub.subscription.subscriptionrequest.client.S3FeignClient;
import com.unihub.subscription.subscriptionrequest.dto.request.UploadFileRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class S3FallBack  implements S3FeignClient {
    @Override
    public ResponseEntity<Void> uploadFile(UploadFileRequest request) {
        log.error("Feign client fallback triggered: S3 service is unavailable. Request: {}", request);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
