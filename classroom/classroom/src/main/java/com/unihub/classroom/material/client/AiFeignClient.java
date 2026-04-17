package com.unihub.classroom.material.client;


import com.unihub.classroom.material.client.fallback.AiFallBack;
import com.unihub.classroom.material.dto.request.MaterialMetaData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "ai", fallback = AiFallBack.class)
public interface AiFeignClient {
    @PostMapping(value = "/ingest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> upload(@RequestPart("file") MultipartFile file,@RequestPart("metadata") MaterialMetaData materialMetaData);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") String id);
}
