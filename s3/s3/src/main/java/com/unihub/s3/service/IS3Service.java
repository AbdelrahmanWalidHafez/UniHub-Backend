package com.unihub.s3.service;

import com.unihub.s3.dto.response.FileResponse;

import java.io.IOException;

public interface IS3Service {

    void uploadFile(byte[] file,String contentType, String key) throws IOException;

    FileResponse downloadFile(String Key);

    void deleteFile(String key);
}
