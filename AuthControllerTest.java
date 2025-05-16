package com.Gestion_Note.Note.Controllers;

import com.Gestion_Note.Note.DTO.AuthRequestDTO;
import com.Gestion_Note.Note.DTO.AuthResponseDTO;
import com.Gestion_Note.Note.DTO.RegisterUserDTO;
import com.Gestion_Note.Note.Services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister_success() throws Exception {
        RegisterUserDTO registerDTO = new RegisterUserDTO();
        registerDTO.setEmail("user@example.com");
        registerDTO.setPassword("password123");
        registerDTO.setName("John");
        registerDTO.setFamilyName("Doe");
        // set other fields as needed

        AuthResponseDTO responseDTO = new AuthResponseDTO();
        responseDTO.setToken("dummy.jwt.token");

        Mockito.when(authenticationService.register(any(RegisterUserDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy.jwt.token"));
    }

    @Test
    void testLogin_success() throws Exception {
        AuthRequestDTO loginRequest = new AuthRequestDTO();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password123");

        AuthResponseDTO responseDTO = new AuthResponseDTO();
        responseDTO.setToken("dummy.jwt.token");

        Mockito.when(authenticationService.authenticate(any(AuthRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy.jwt.token"));
    }
}
