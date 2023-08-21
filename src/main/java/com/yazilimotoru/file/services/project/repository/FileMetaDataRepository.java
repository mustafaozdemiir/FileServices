package com.yazilimotoru.file.services.project.repository;


import com.yazilimotoru.file.services.project.models.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaDataRepository extends JpaRepository<FileMetadata, Long> {
    boolean existsByNameAndPath(String name, String path);

}
