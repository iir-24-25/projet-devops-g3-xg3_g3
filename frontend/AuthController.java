package com.Gestion_Note.Note.Controllers;



import com.Gestion_Note.Note.DTO.AuthRequestDTO;
import com.Gestion_Note.Note.DTO.AuthResponseDTO;
import com.Gestion_Note.Note.DTO.RegisterUserDTO;
import com.Gestion_Note.Note.Services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterUserDTO dto) throws Exception {
        return ResponseEntity.ok(authenticationService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authRequest));
    }
}
