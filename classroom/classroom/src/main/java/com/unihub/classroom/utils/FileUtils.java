package com.unihub.classroom.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unihub.classroom.clazz.model.ClassRoom;
import com.unihub.classroom.material.client.AiFeignClient;
import com.unihub.classroom.material.client.S3FeignClient;
import com.unihub.classroom.material.dto.request.DeleteFileRequest;
import com.unihub.classroom.material.dto.request.MaterialMetaData;
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

    private final ObjectMapper objectMapper;

    private final StreamBridge streamBridge;

    private final AiFeignClient aiFeignClient;

    private final S3FeignClient s3FeignClient;

    /**
     * Uploads a list of files to S3 Bucket and attaches their generated URLs to the given target entity.
     * <p>
     * For each file, a unique storage key is generated based on the file and classroom context. The file is then
     * uploaded using the configured S3 Bucket mechanism, and the resulting public URL is added to the target entity's
     * list of file URLs via the provided URL getter.
     *
     * @author Abdelrahman Walid
     *
     * @since <a href="https://github.com/AbdelrahmanWalidHafez/UniHub-Backend/tree/Release/1.5.1">Release/1.5.1</a>
     *
     * @param files        the list of files to upload; must not be null
     * @param classRoom    the classroom context used to generate unique file storage keys
     * @param target       the target entity that will receive the uploaded file URLs (e.g., Material or Submission)
     * @param urlGetter    a function that returns the mutable list of file URLs from the target entity
     * @param <T>          the type of the target entity
     * @throws IOException  if an I/O error occurs during file processing or upload
     *
     * @see com.unihub.classroom.material.model.Material
     * @see com.unihub.classroom.assginement.model.Submission
     * @see Function
     */
    public <T> void uploadFiles(List<MultipartFile> files, ClassRoom classRoom, T target, Function<T, List<String>> urlGetter, Material material) throws IOException {
        List<String> urls = urlGetter.apply(target);
        for (MultipartFile file : files) {
            String key = generateKey(file, classRoom);
            urls.add(bucketLink + key);
            if (target instanceof Material) {
                uploadFile(file, key, classRoom, material);
            }
            else{
                uploadFile(file, key);
            }
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

    private void uploadFile(MultipartFile file, String key,ClassRoom classRoom,Material material) throws IOException {
        uploadFile(file, key);
        aiFeignClient.upload(file,
                objectMapper.writeValueAsString(
                        MaterialMetaData.builder()
                                .classroomId(classRoom.getId())
                                .classSubTitle(classRoom.getClassSubTitle())
                                .classTitle(classRoom.getClassTitle())
                                .collegeId(classRoom.getCollegeId())
                                .universityId(classRoom.getUniversityId())
                                .materialId(material.getMid())
                                .headLine(material.getHeadLine())
                                .description(material.getDescription())
                                .materialType(material.getMaterialType())
                                .build()));
    }
    private void uploadFile(MultipartFile file, String key) throws IOException {
        s3FeignClient.uploadFile(UploadFileRequest.builder()
                .fileContent(file.getBytes())
                .key(key)
                .contentType(file.getContentType())
                .build());
    }
}
