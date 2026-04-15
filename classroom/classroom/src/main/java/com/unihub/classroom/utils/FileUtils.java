package com.unihub.classroom.utils;

import com.unihub.classroom.assginement.model.Submission;
import com.unihub.classroom.clazz.model.ClassRoom;
import com.unihub.classroom.material.client.S3FeignClient;
import com.unihub.classroom.material.dto.request.DeleteFileRequest;
import com.unihub.classroom.material.dto.request.UploadFileRequest;
import com.unihub.classroom.material.model.Material;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class FileUtils {


    @Value("${aws.bucket}")
    private String bucketLink;

    private final StreamBridge streamBridge;

    private final S3FeignClient s3FeignClient;

    public <T> void uploadFiles(List<MultipartFile> files, ClassRoom classRoom, T target, Function<T, List<String>> urlGetter) throws IOException {
        List<String> urls = urlGetter.apply(target);
        for (MultipartFile file : files) {
            String key = generateKey(file, classRoom);
            urls.add(bucketLink + key);
            UploadFileRequest request = UploadFileRequest.builder()
                    .fileContent(file.getBytes())
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            uploadFile(request);
        }
    }

    public void deleteFile(String key) {
        if (key != null) {
            streamBridge.send("deleteFile-out-0", DeleteFileRequest.builder().key(key.replace(bucketLink,"")).build());
        }
    }

    private String generateKey(MultipartFile file, ClassRoom classRoom){
        String extension = getExtension(file.getOriginalFilename());
        return   classRoom.getCode() + "/" + file.getOriginalFilename() + "." + extension;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "bin";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    private void uploadFile(UploadFileRequest uploadFileRequest){
        s3FeignClient.uploadFile(uploadFileRequest);
    }

}
