package com.Gestion_Note.Note.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthResponseDTOTest {

    @Test
    public void testConstructorAndGetter() {
        String token = "sample-token-123";
        AuthResponseDTO responseDTO = new AuthResponseDTO(token);

        assertEquals(token, responseDTO.getToken());
    }

    @Test
    public void testSetterAndGetter() {
        AuthResponseDTO responseDTO = new AuthResponseDTO(null);
        String newToken = "new-token-456";

        responseDTO.setToken(newToken);

        assertEquals(newToken, responseDTO.getToken());
    }
}
