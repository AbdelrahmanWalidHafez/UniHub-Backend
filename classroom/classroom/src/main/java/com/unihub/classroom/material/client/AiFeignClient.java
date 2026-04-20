package com.unihub.classroom.material.client;


import com.unihub.classroom.material.client.fallback.AiFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@FeignClient(name = "ai", fallback = AiFallBack.class)
public interface AiFeignClient {

    @PostMapping(value = "/api/v1/material/ingest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> upload(@RequestPart("file") MultipartFile file,@RequestPart("metadata") String materialMetaData);

    @DeleteMapping("/api/v1/material/delete/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") UUID id);
}
