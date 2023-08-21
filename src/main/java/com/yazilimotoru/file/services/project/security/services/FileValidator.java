package com.yazilimotoru.file.services.project.security.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileValidator {
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("png", "jpeg", "jpg", "docx", "pdf", "xlsx");

    public static boolean isValidFile(MultipartFile file) {
        if (file.isEmpty() || file.getSize() > MAX_FILE_SIZE) {
            return false;
        }

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase());
    }
}

