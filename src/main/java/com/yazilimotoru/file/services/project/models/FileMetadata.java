package com.yazilimotoru.file.services.project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;

@Entity
@Table(name = "file_metadata",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id"),
                @UniqueConstraint(columnNames = "path")
        })
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String extension;
    private String path;
    private Long size;

    public FileMetadata() {
    }

    public FileMetadata(String name, String extension, String path, Long size) {
        this.name = name;
        this.extension = extension;
        this.path = path;
        this.size = size;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileMetadata) {
            FileMetadata fileMetadata = (FileMetadata) obj;
            return this.id.equals(fileMetadata.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
