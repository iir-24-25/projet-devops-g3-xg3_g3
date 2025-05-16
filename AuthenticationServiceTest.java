package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.DTO.AuthRequestDTO;
import com.Gestion_Note.Note.DTO.AuthResponseDTO;
import com.Gestion_Note.Note.DTO.RegisterUserDTO;
import com.Gestion_Note.Note.Entities.*;
import com.Gestion_Note.Note.Repository.*;
import com.Gestion_Note.Note.Security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ParentRopository parentRopository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_student_success() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setRole(Roles.STUDENT);
        dto.setEmail("student@example.com");
        dto.setUsername("studentUser");
        dto.setName("Student Name");
        dto.setPassword("pass123");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPass");
        when(jwtService.generateToken(any(CustomUserDetails.class))).thenReturn("jwt-token");

        AuthResponseDTO response = authenticationService.register(dto);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void register_emailAlreadyExists_throws() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setEmail("exists@example.com");
        dto.setRole(Roles.TEACHER);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.register(dto));

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void register_noRole_throws() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setEmail("user@example.com");
        dto.setRole(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.register(dto));

        assertEquals("Role is required", exception.getMessage());
    }

    @Test
    void register_unsupportedRole_throws() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setEmail("user@example.com");
        dto.setRole(null); // Simulate unsupported role

    }

    @Test
    void authenticate_success() {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setEmail("user@example.com");
        request.setPassword("rawPassword");

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword("encodedPass");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(any(CustomUserDetails.class))).thenReturn("jwt-token");

        AuthResponseDTO response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void authenticate_userNotFound_throws() {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setEmail("nouser@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticate(request));

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void authenticate_invalidPassword_throws() {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setEmail("user@example.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword("encodedPass");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticate(request));

        assertEquals("Invalid password", exception.getMessage());
    }
}
