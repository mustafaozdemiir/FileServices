package com.yazilimotoru.file.services.project;
import com.yazilimotoru.file.services.project.payload.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ComponentTest {

    @Test
    public void testSignupRequestComponent() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("deneme");
        signupRequest.setEmail("example@example.com");
        signupRequest.setPassword("123456");

        assertNotNull(signupRequest);
        assertEquals("deneme", signupRequest.getUsername());
        assertEquals("example@example.com", signupRequest.getEmail());
        assertEquals("123456", signupRequest.getPassword());
    }
}
