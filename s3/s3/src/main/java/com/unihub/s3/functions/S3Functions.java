package com.unihub.s3.functions;

import com.unihub.s3.dto.request.DeleteFileRequest;
import com.unihub.s3.dto.request.UploadFileRequest;
import com.unihub.s3.service.IS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Base64;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class S3Functions {

    private final IS3Service s3Service;

    @Bean
     public Consumer<DeleteFileRequest> deleteFile () {
        return deleteFileRequest -> s3Service.deleteFile(deleteFileRequest.getKey());
    }
}
