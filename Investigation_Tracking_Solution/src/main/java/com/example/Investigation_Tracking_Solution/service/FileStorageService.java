package com.example.Investigation_Tracking_Solution.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file, String storedFileName);
    Resource loadFileAsResource(String storagePath);
    void deleteFile(String storagePath);
}
