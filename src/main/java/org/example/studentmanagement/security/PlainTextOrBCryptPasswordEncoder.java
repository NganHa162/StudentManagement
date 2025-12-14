package org.example.studentmanagement.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Password encoder that supports both plain text and BCrypt hashed passwords.
 * This is useful for migration or testing purposes.
 * 
 * When encoding: Always uses BCrypt
 * When matching: Tries BCrypt first, then plain text comparison
 */
public class PlainTextOrBCryptPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        // Always encode new passwords with BCrypt
        return bcryptEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || rawPassword == null) {
            return false;
        }

        // Try BCrypt first
        try {
            if (bcryptEncoder.matches(rawPassword, encodedPassword)) {
                return true;
            }
        } catch (Exception e) {
            // Not a BCrypt hash, continue to plain text check
        }

        // If BCrypt doesn't match, try plain text comparison
        // This allows plain text passwords in database for testing
        return encodedPassword.equals(rawPassword.toString());
    }
}

