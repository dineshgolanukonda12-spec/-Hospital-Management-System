package com.hospital.backend;

import com.hospital.backend.entity.PasswordResetToken;
import com.hospital.backend.entity.User;
import com.hospital.backend.repository.PasswordResetTokenRepository;
import com.hospital.backend.repository.UserRepository;
import com.hospital.backend.service.PasswordResetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordResetTest {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Test
    void testFullPasswordResetFlow() {
        // Ensure test user exists
        String testEmail = "reset.test@example.com";
        User user = userRepository.findByEmail(testEmail).orElseGet(() -> {
            return userRepository.save(new User("Reset Tester", testEmail, "initialPassword123", "9999999999", "patient"));
        });

        // 1. Request password reset
        String responseMessage = passwordResetService.requestPasswordReset(testEmail);
        assertNotNull(responseMessage);
        assertTrue(responseMessage.contains("password reset instructions have been sent"));

        // Retrieve the generated token from repository
        List<PasswordResetToken> activeTokens = tokenRepository.findByUserAndUsedFalse(user);
        assertFalse(activeTokens.isEmpty(), "Active token should be created");
        PasswordResetToken storedToken = activeTokens.get(0);
        assertNotNull(storedToken.getTokenHash());
        assertFalse(storedToken.isUsed());
        assertFalse(storedToken.isExpired());

        // 2. Test invalid raw token verification
        assertFalse(passwordResetService.verifyToken("completely-invalid-raw-token"));

        // 3. Test reset password with invalid token
        assertThrows(IllegalArgumentException.class, () -> {
            passwordResetService.resetPassword("invalid-token", "newSecretPassword123");
        });

        // 4. Manually test token verification via known token hash
        // Note: Raw token was hashed before storing. Let's create an explicit token pair to test end-to-end reset:
        String testRawToken = "test-raw-token-1234567890123456";
        String testHash = passwordResetService.hashToken(testRawToken);
        PasswordResetToken customToken = new PasswordResetToken(user, testHash, java.time.LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(customToken);

        assertTrue(passwordResetService.verifyToken(testRawToken));

        // 5. Execute password reset
        passwordResetService.resetPassword(testRawToken, "newSecretPassword123");

        // Verify user password was changed
        User updatedUser = userRepository.findByEmail(testEmail).orElseThrow();
        assertEquals("newSecretPassword123", updatedUser.getPassword());

        // 6. Verify single-use policy: token is now marked as used
        assertFalse(passwordResetService.verifyToken(testRawToken));
        assertThrows(IllegalStateException.class, () -> {
            passwordResetService.resetPassword(testRawToken, "anotherPassword456");
        });

        // Clean up
        tokenRepository.delete(customToken);
        tokenRepository.delete(storedToken);
        userRepository.delete(user);
    }
}
