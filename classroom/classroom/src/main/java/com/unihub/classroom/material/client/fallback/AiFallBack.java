package com.unihub.classroom.material.client.fallback;

import com.unihub.classroom.material.client.AiFeignClient;
import com.unihub.classroom.material.dto.request.MaterialMetaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class AiFallBack implements AiFeignClient {
    @Override
    public ResponseEntity<Void> upload(MultipartFile file, MaterialMetaData materialMetaData) {
        log.error("Feign client fallback triggered: S3 service is unavailable. Request: {}", materialMetaData);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        log.error("Feign client fallback triggered: S3 service is unavailable. Request: {}", id);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
