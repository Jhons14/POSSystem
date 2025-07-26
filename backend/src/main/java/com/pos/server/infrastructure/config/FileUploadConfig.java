package com.pos.server.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class FileUploadConfig {
    
    @Value("${application.allowed-file-types:jpg,jpeg,png,gif,webp}")
    private String allowedFileTypesString;
    
    public List<String> getAllowedFileTypes() {
        return Arrays.asList(allowedFileTypesString.split(","));
    }
    
    public boolean isFileTypeAllowed(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return false;
        }
        
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return getAllowedFileTypes().contains(extension);
    }
}