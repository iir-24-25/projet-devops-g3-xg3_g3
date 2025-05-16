package com.Gestion_Note.Note.DTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AuthRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testGettersAndSetters() {
        AuthRequestDTO dto = new AuthRequestDTO();

        String email = "test@example.com";
        String password = "mypassword";

        dto.setEmail(email);
        dto.setPassword(password);

        assertEquals(email, dto.getEmail());
        assertEquals(password, dto.getPassword());
    }

    @Test
    public void testValidation_ValidData() {
        AuthRequestDTO dto = new AuthRequestDTO();
        dto.setEmail("valid@example.com");
        dto.setPassword("securepassword");

        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidation_InvalidEmail() {
        AuthRequestDTO dto = new AuthRequestDTO();
        dto.setEmail("invalid-email");
        dto.setPassword("password123");

        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        boolean emailErrorFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email") &&
                        v.getMessage().contains("Invalid email format"));
        assertTrue(emailErrorFound);
    }

    @Test
    public void testValidation_BlankFields() {
        AuthRequestDTO dto = new AuthRequestDTO();
        dto.setEmail("");
        dto.setPassword("");

        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertEquals(2, violations.size()); // Both email
