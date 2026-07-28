package com.example.Investigation_Tracking_Solution.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class FileStorageProperties {

    @Value("${file.upload.dir:uploads/evidence}")
    private String uploadDir;
}
