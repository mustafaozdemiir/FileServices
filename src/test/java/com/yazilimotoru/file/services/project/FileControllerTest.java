package com.yazilimotoru.file.services.project;
import com.yazilimotoru.file.services.project.controllers.FileController;
import com.yazilimotoru.file.services.project.models.FileMetadata;
import com.yazilimotoru.file.services.project.security.services.FileMetaDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FileControllerTest {
    @Mock
    private FileMetaDataService fileMetaDataService;
    @InjectMocks
    private FileController fileController;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetAllFiles() {
        List<FileMetadata> files = new ArrayList<>();
        files.add(new FileMetadata());
        when(fileMetaDataService.getAllFiles()).thenReturn(files);

        ResponseEntity<List<FileMetadata>> response = fileController.getAllFiles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(files, response.getBody());
    }
     @Test
     @WithMockUser(roles = "USER")
     public void testUploadValidFile() {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.png", MediaType.IMAGE_PNG_VALUE, "test content".getBytes()
        );

        when(fileMetaDataService.saveFile(multipartFile)).thenReturn(new FileMetadata());

        ResponseEntity<String> response = fileController.uploadFile(multipartFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The file has been uploaded successfully.", response.getBody());
        verify(fileMetaDataService, times(1)).saveFile(multipartFile);
    }
    @Test
    public void testUploadInvalidFileType() {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.exe", "application/octet-stream", new byte[5 * 1024 * 1024]);

        ResponseEntity<String> response = fileController.uploadFile(multipartFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The file type is invalid. Valid types (png, jpeg, jpg, docx, pdf, xlsx) Maximum Size 5Mb", response.getBody());
        verify(fileMetaDataService, times(0)).saveFile(multipartFile);
    }
    @Test
    public void testUploadInvalidFileSize() {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, new byte[6 * 1024 * 1024]);

        ResponseEntity<String> response = fileController.uploadFile(multipartFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The file type is invalid. Valid types (png, jpeg, jpg, docx, pdf, xlsx) Maximum Size 5Mb", response.getBody());
        verify(fileMetaDataService, times(0)).saveFile(multipartFile);
    }

}
