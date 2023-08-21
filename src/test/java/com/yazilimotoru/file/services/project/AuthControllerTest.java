package com.yazilimotoru.file.services.project;

import com.yazilimotoru.file.services.project.controllers.AuthController;
import com.yazilimotoru.file.services.project.models.ERole;
import com.yazilimotoru.file.services.project.models.Role;
import com.yazilimotoru.file.services.project.models.User;
import com.yazilimotoru.file.services.project.payload.request.LoginRequest;
import com.yazilimotoru.file.services.project.payload.request.SignupRequest;
import com.yazilimotoru.file.services.project.payload.response.JwtResponse;
import com.yazilimotoru.file.services.project.payload.response.MessageResponse;
import com.yazilimotoru.file.services.project.repository.RoleRepository;
import com.yazilimotoru.file.services.project.repository.UserRepository;
import com.yazilimotoru.file.services.project.security.jwt.JwtUtils;
import com.yazilimotoru.file.services.project.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;


    @Test
    public void testAuthenticateUser() {
        // Test input
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("123456");

        // Mock authentication
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Mock UserDetailsImpl
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "user", "user@gmail.com", "123456", new ArrayList<>());
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Mock JwtUtils
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("testjwt");

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert response and content
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof JwtResponse);

        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("testjwt", jwtResponse.getAccessToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("user", jwtResponse.getUsername());
        assertEquals("user@gmail.com", jwtResponse.getEmail());
        assertTrue(jwtResponse.getRoles().isEmpty());
    }

    @Test
    public void testRegisterUser() {
        // Test input
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("newuser");
        signUpRequest.setEmail("newuser@example.com");
        signUpRequest.setPassword("newpassword");
        signUpRequest.setRole(Collections.singleton("user"));

        // Mock userRepository
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);

        // Mock roleRepository
        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        // Mock encoder
        when(encoder.encode("newpassword")).thenReturn("encodedPassword");

        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert response and content
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof MessageResponse);

        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());

        // Verify userRepository.save method is called
        verify(userRepository).save(any(User.class));
    }
}
