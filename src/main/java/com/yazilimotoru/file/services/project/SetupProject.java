package com.yazilimotoru.file.services.project;


import com.yazilimotoru.file.services.project.models.ERole;
import com.yazilimotoru.file.services.project.models.Role;
import com.yazilimotoru.file.services.project.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Optional;

@Configuration
public class SetupProject {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository) {
        return args -> {
            Optional<Role> roleOptional = roleRepository.findByName(ERole.ROLE_USER);
            if (roleOptional.isPresent()) {
            } else {
                Role userRole = new Role(ERole.ROLE_USER);
                roleRepository.save(userRole);

            }

            String uploadPath = "upload-storage";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                if (uploadDir.mkdir()) {
                } else {

                }
            }
        };
    }
}
