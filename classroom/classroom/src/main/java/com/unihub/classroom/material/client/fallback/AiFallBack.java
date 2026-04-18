package com.unihub.classroom.material.client.fallback;

import com.unihub.classroom.material.client.AiFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Component
public class AiFallBack implements AiFeignClient {

    public static final String ERROR_MESSAGE = "Feign client fallback triggered: ai service is unavailable. Request: {}";
    @Override
    public ResponseEntity<Void> upload(MultipartFile file, String materialMetaData) {
        log.error(ERROR_MESSAGE, materialMetaData);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        log.error(ERROR_MESSAGE, id);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
