package com.yazilimotoru.file.services.project.security.services;

import com.yazilimotoru.file.services.project.models.FileMetadata;
import com.yazilimotoru.file.services.project.repository.FileMetaDataRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class FileMetaDataServiceImpl implements FileMetaDataService {
    private final FileMetaDataRepository fileMetadataRepository;

    public FileMetaDataServiceImpl(FileMetaDataRepository fileMetadataRepository) {
        this.fileMetadataRepository = fileMetadataRepository;
    }
    @Override
    public List<FileMetadata> getAllFiles() {
        return fileMetadataRepository.findAll();
    }

    public FileMetadata saveFilePath(MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = FilenameUtils.getExtension(fileName);
        String filePath = "upload-storage" + File.separator + fileName;
        if (fileMetadataRepository.existsByNameAndPath(fileName, filePath)) {
            throw new RuntimeException("A file already exists in the same filename and path.");
        }
        try{
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            FileMetadata fileMetadata = new FileMetadata();
            fileMetadata.setName(fileName);
            fileMetadata.setExtension(fileExtension);
            fileMetadata.setPath(filePath);
            fileMetadata.setSize(file.getSize());
            return fileMetadata;
        }catch (IOException e){
            e.printStackTrace();
            return null;

        }

    }
    @Override
    public FileMetadata saveFile(MultipartFile file) {
            return fileMetadataRepository.save(saveFilePath(file));
    }
    @Override
    public FileMetadata getFileById(Long id) {
        return fileMetadataRepository.findById(id).orElse(null);
    }
    @Override
    public byte[] getFileContentById(Long id) {
        FileMetadata fileMetadata = getFileById(id);
        if (fileMetadata != null) {
            try {
                return Files.readAllBytes(Paths.get(fileMetadata.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    public void deleteFileById(Long id) {
        FileMetadata fileMetadata = fileMetadataRepository.findById(id).orElse(null);
        if (fileMetadata != null) {
            String filePath = "upload-storage/" + fileMetadata.getName();
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                try {
                    Files.delete(path);
                    fileMetadataRepository.delete(fileMetadata);
                } catch (IOException e) {
                    throw new RuntimeException("An error occurred while deleting the file.");
                }
            } else {
                throw new RuntimeException("The file you want to delete was not found.");
            }
        } else {
            throw new RuntimeException("The file you want to delete could not be found.");
        }
    }

    public boolean isFileExists(Long id) {
        return fileMetadataRepository.existsById(id);
    }
    public FileMetadata updateFile(Long id, MultipartFile newFile) {
        Optional<FileMetadata> existingFileOptional = fileMetadataRepository.findById(id);
        if (existingFileOptional.isEmpty()) {
            return null;
        }
        FileMetadata existingFile = existingFileOptional.get();
        if (!FileValidator.isValidFile(newFile)) {
            return null;
        }
        try {
            FileMetadata newFileData=saveFilePath(newFile);
            newFileData.setId(id);

            String newFilePath = "upload-storage" + File.separator + StringUtils.cleanPath(newFile.getOriginalFilename());

            Files.copy(newFile.getInputStream(), Paths.get(newFilePath), StandardCopyOption.REPLACE_EXISTING);

            existingFile.setName(newFile.getOriginalFilename());
            existingFile.setSize(newFile.getSize());

            Path oldPath = Paths.get(existingFile.getPath());

            if (Files.exists(oldPath)) {
                Files.delete(oldPath);
            }

            return fileMetadataRepository.save(newFileData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }}

