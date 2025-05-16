package com.Gestion_Note.Note.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginDTOTest {

    @Test
    public void testGettersAndSetters() {
        LoginDTO loginDTO = new LoginDTO();

        String testUsername = "testUser";
        String testPassword = "testPassword";

        loginDTO.setUsername(testUsername);
        loginDTO.setPassword(testPassword);

        assertEquals(testUsername, loginDTO.getUsername());
        assertEquals(testPassword, loginDTO.getPassword());
    }
}
