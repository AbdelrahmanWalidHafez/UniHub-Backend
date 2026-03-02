package com.unihub.s3.service.impl;


import com.unihub.s3.config.S3ConfigurationProperties;
import com.unihub.s3.dto.response.FileResponse;
import com.unihub.s3.service.IS3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements IS3Service {

    private final S3Client s3Client;

    private final S3ConfigurationProperties s3ConfigurationProperties;

    @Override
    public void uploadFile(byte[]file,String contentType,String key) {
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(s3ConfigurationProperties.bucketname())
                .key(key)
                .contentType(contentType)
                .build(), RequestBody.fromBytes(file));
    }

    @Override
    public FileResponse downloadFile(String key) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(s3ConfigurationProperties.bucketname())
                    .key(key)
                    .build());
        } catch (NoSuchKeyException e) {
            throw new EntityNotFoundException("there is no file with the following key: " + key);
        }
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(s3ConfigurationProperties.bucketname())
                        .key(key)
                        .build(),
                ResponseTransformer.toBytes()
        );
        GetObjectResponse response = objectBytes.response();
        String contentType = response.contentType();
        if (contentType == null) contentType = "application/octet-stream";
        return new FileResponse(objectBytes.asByteArray(), contentType);
    }

    @Override
    public void deleteFile(String key){
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(s3ConfigurationProperties.bucketname())
                .key(key)
                .build());
    }

}
