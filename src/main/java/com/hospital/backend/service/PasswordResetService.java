package com.hospital.backend.service;

import com.hospital.backend.entity.PasswordResetToken;
import com.hospital.backend.entity.User;
import com.hospital.backend.repository.PasswordResetTokenRepository;
import com.hospital.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

@Service
public class PasswordResetService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);
    private static final int TOKEN_EXPIRY_MINUTES = 15;
    private static final int MAX_REQUESTS_IN_WINDOW = 5;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public PasswordResetService(UserRepository userRepository,
                                PasswordResetTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    /**
     * Request a password reset. Returns a status result.
     * Note: A generic confirmation message is always shown to the caller to avoid account enumeration.
     */
    @Transactional
    public String requestPasswordReset(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "If an account exists with that email, password reset instructions have been sent.";
        }

        Optional<User> userOptional = userRepository.findByEmail(email.trim().toLowerCase());
        if (userOptional.isEmpty()) {
            // Generic response to prevent enumeration
            log.info("Password reset requested for non-existent email: {}", email);
            return "If an account exists with that email, password reset instructions have been sent.";
        }

        User user = userOptional.get();

        // Rate limiting: Check how many tokens were generated in the last 15 minutes
        long recentRequests = tokenRepository.countByUserAndCreatedAtAfter(user, LocalDateTime.now().minusMinutes(TOKEN_EXPIRY_MINUTES));
        if (recentRequests >= MAX_REQUESTS_IN_WINDOW) {
            log.warn("Rate limit exceeded for password reset on email: {}", email);
            throw new IllegalStateException("Too many password reset attempts. Please wait a few minutes before trying again.");
        }

        // Invalidate previous active tokens for this user
        List<PasswordResetToken> existingTokens = tokenRepository.findByUserAndUsedFalse(user);
        for (PasswordResetToken t : existingTokens) {
            t.setUsed(true);
        }
        tokenRepository.saveAll(existingTokens);

        // Generate cryptographically secure 32-byte token
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // Hash token using SHA-256 before persisting
        String tokenHash = hashToken(rawToken);

        // Expiration time: 15 minutes
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(TOKEN_EXPIRY_MINUTES);

        PasswordResetToken resetToken = new PasswordResetToken(user, tokenHash, expiryDate);
        tokenRepository.save(resetToken);

        // Construct reset link
        String resetLink = "http://localhost:5173/reset-password?token=" + rawToken;

        // In a production environment, send email via JavaMailSender using environment variables:
        // System.getenv("MAIL_HOST"), System.getenv("MAIL_USERNAME"), etc.
        // For development/demonstration, log the secure reset URL to console.
        log.info("=================================================================");
        log.info("PASSWORD RESET LINK GENERATED FOR: {}", user.getEmail());
        log.info("RESET URL: {}", resetLink);
        log.info("EXPIRES AT: {} (15 minutes)", expiryDate);
        log.info("=================================================================");

        return "If an account exists with that email, password reset instructions have been sent.";
    }

    /**
     * Verifies if a reset token is valid and not expired.
     */
    public boolean verifyToken(String rawToken) {
        if (rawToken == null || rawToken.trim().isEmpty()) {
            return false;
        }

        String tokenHash = hashToken(rawToken.trim());
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByTokenHash(tokenHash);

        if (tokenOptional.isEmpty()) {
            return false;
        }

        PasswordResetToken token = tokenOptional.get();
        return !token.isUsed() && !token.isExpired();
    }

    /**
     * Resets user password using valid token.
     */
    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        if (rawToken == null || rawToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Reset token is required.");
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }

        String tokenHash = hashToken(rawToken.trim());
        PasswordResetToken token = tokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or unrecognized password reset token."));

        if (token.isUsed()) {
            throw new IllegalStateException("This password reset link has already been used. Please request a new one.");
        }

        if (token.isExpired()) {
            throw new IllegalStateException("This password reset link has expired. Please request a new one.");
        }

        User user = token.getUser();
        user.setPassword(newPassword);
        userRepository.save(user);

        // Mark token as used immediately (single-use token)
        token.setUsed(true);
        tokenRepository.save(token);

        log.info("Password successfully reset for user: {}", user.getEmail());
    }

    /**
     * Hashes raw token using SHA-256 for secure database storage.
     */
    public String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
