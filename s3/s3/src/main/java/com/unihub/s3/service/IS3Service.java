package com.unihub.s3.service;

import com.unihub.s3.dto.request.UploadFileRequest;
import com.unihub.s3.dto.response.FileResponse;

import java.io.IOException;

public interface IS3Service {

    void uploadFile(UploadFileRequest request) throws IOException;

    FileResponse downloadFile(String Key);

    void deleteFile(String key);
}
