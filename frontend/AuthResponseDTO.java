package com.Gestion_Note.Note.DTO;


import lombok.Data;

@Data
public class AuthResponseDTO {

    private String token;

    public AuthResponseDTO(String token) {
        this.token = token;
    }
}
