package com.unihub.s3.controller;

import com.unihub.s3.dto.request.UploadFileRequest;
import com.unihub.s3.dto.response.FileResponse;
import com.unihub.s3.service.IS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class S3Controller {

    private final IS3Service s3Service;

    @GetMapping("/get-file/{key}")
    public ResponseEntity<byte[]> getAccreditation(@PathVariable String key) {
        FileResponse file = s3Service.downloadFile(key);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(key).build().toString())
                .body(file.getContent());
    }

    @PostMapping("/upload-file")
    public ResponseEntity<Void> uploadFile(@RequestBody UploadFileRequest request) throws IOException {
        s3Service.uploadFile(request);
        return ResponseEntity.accepted().build();
    }

}
