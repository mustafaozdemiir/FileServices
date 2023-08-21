package com.yazilimotoru.file.services.project.security.services;

import com.yazilimotoru.file.services.project.models.FileMetadata;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface FileMetaDataService {
    List<FileMetadata> getAllFiles();
    FileMetadata saveFile(MultipartFile file);
    FileMetadata getFileById(Long id);
    byte[] getFileContentById(Long id);
    void deleteFileById(Long id);
    public boolean isFileExists(Long id);
    FileMetadata updateFile(Long id, MultipartFile file);

}
