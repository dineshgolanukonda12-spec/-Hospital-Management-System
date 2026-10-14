package com.hospital.backend;

import com.hospital.backend.entity.User;
import com.hospital.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    void testAuthLoginSuccess() {
        User user = authService.login("admin@hospital.com", "admin123", "admin");
        assertNotNull(user);
        assertEquals("admin@hospital.com", user.getEmail());
    }

    @Test
    void testAuthLoginFailure() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.login("admin@hospital.com", "wrongpassword", "admin");
        });
    }
}
