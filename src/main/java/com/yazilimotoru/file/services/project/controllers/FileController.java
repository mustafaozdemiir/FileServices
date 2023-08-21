package com.yazilimotoru.file.services.project.controllers;

import com.yazilimotoru.file.services.project.models.FileMetadata;
import com.yazilimotoru.file.services.project.security.services.FileMetaDataService;
import com.yazilimotoru.file.services.project.security.services.FileValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileMetaDataService fileMetaDataService;

    public FileController(FileMetaDataService fileMetaDataService) {
        this.fileMetaDataService = fileMetaDataService;
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<FileMetadata>> getAllFiles() {
        List<FileMetadata> files = fileMetaDataService.getAllFiles();
        return ResponseEntity.ok(files);
    }
    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (!FileValidator.isValidFile(file)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file type is invalid. Valid types (png, jpeg, jpg, docx, pdf, xlsx) Maximum Size 5Mb");
        } else {
            FileMetadata savedFile = fileMetaDataService.saveFile(file);
            if (savedFile != null) {
                return ResponseEntity.ok("The file has been uploaded successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred while uploading the file.");
            }
        }
    }

    @PutMapping("/replace")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> updateFile(@RequestParam Long id, @RequestParam("file") MultipartFile file) {
        if (!fileMetaDataService.isFileExists(id)) {
            return ResponseEntity.notFound().build();
        }
        if (!FileValidator.isValidFile(file)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file type is invalid. Valid types (png, jpeg, jpg, docx, pdf, xlsx) Maximum Size 5Mb");
        } else {
            FileMetadata updatedFile = fileMetaDataService.updateFile(id, file);
            if (updatedFile != null) {
                return ResponseEntity.ok("The file has been successfully updated.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the file.");
            }
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<FileMetadata> getFileDetails(@PathVariable Long id) {
        FileMetadata fileMetadata = fileMetaDataService.getFileById(id);
        if (fileMetadata != null) {
            return ResponseEntity.ok(fileMetadata);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<byte[]> getFileContent(@PathVariable Long id) {
        byte[] fileContent = fileMetaDataService.getFileContentById(id);
        if (fileContent != null) {
            return ResponseEntity.ok(fileContent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        fileMetaDataService.deleteFileById(id);
        return ResponseEntity.ok("The file was successfully deleted");
    }
}
