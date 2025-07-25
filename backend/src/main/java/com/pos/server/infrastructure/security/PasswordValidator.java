package com.pos.server.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PasswordValidator {

    @Value("${security.password.min-length:8}")
    private int minLength;

    @Value("${security.password.require-uppercase:true}")
    private boolean requireUppercase;

    @Value("${security.password.require-lowercase:true}")
    private boolean requireLowercase;

    @Value("${security.password.require-numbers:true}")
    private boolean requireNumbers;

    @Value("${security.password.require-special-chars:true}")
    private boolean requireSpecialChars;

    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern NUMBERS_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");

    public boolean isValid(String password) {
        return getValidationErrors(password).isEmpty();
    }

    public List<String> getValidationErrors(String password) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.length() < minLength) {
            errors.add("Password must be at least " + minLength + " characters long");
        }

        if (requireUppercase && !UPPERCASE_PATTERN.matcher(password).find()) {
            errors.add("Password must contain at least one uppercase letter");
        }

        if (requireLowercase && !LOWERCASE_PATTERN.matcher(password).find()) {
            errors.add("Password must contain at least one lowercase letter");
        }

        if (requireNumbers && !NUMBERS_PATTERN.matcher(password).find()) {
            errors.add("Password must contain at least one number");
        }

        if (requireSpecialChars && !SPECIAL_CHARS_PATTERN.matcher(password).find()) {
            errors.add("Password must contain at least one special character");
        }

        return errors;
    }

    public String getPasswordRequirements() {
        List<String> requirements = new ArrayList<>();
        requirements.add("At least " + minLength + " characters");
        
        if (requireUppercase) requirements.add("One uppercase letter");
        if (requireLowercase) requirements.add("One lowercase letter");
        if (requireNumbers) requirements.add("One number");
        if (requireSpecialChars) requirements.add("One special character");

        return "Password must contain: " + String.join(", ", requirements);
    }
}