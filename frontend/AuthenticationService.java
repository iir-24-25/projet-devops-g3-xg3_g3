package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.DTO.AuthRequestDTO;
import com.Gestion_Note.Note.DTO.AuthResponseDTO;
import com.Gestion_Note.Note.DTO.RegisterUserDTO;
import com.Gestion_Note.Note.Entities.*;
import com.Gestion_Note.Note.Repository.*;
import com.Gestion_Note.Note.Security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ParentRopository parentRopository;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            AdminRepository adminRepository,
            TeacherRepository teacherRepository,
            StudentRepository studentRepository, ParentRopository parentRopository
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.parentRopository = parentRopository;
    }

    public AuthResponseDTO register(RegisterUserDTO dto) {
        if (dto.getRole() == null) {
            throw new IllegalArgumentException("Role is required");
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user;

        switch (dto.getRole()) {
            case STUDENT:
                Student student = new Student();
                populateUserFields(student, dto);
                studentRepository.save(student);
                user = student;
                break;

            case TEACHER:
                Teacher teacher = new Teacher();
                populateUserFields(teacher, dto);
                teacherRepository.save(teacher);
                user = teacher;
                break;

            case ADMIN:
                Admin admin = new Admin();
                populateUserFields(admin, dto);
                adminRepository.save(admin);
                user = admin;
                break;

            case PARENT:
                Parent parent = new Parent();
                populateUserFields(parent, dto);
                parentRopository.save(parent);
                user = parent;
                break;

            default:
                throw new IllegalArgumentException("Unsupported role: " + dto.getRole());
        }

        String token = jwtService.generateToken(new CustomUserDetails(user));
        return new AuthResponseDTO(token);
    }

    private void populateUserFields(User user, RegisterUserDTO dto) {
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
    }

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        String token = jwtService.generateToken(new CustomUserDetails(user));
        return new AuthResponseDTO(token);
    }
}
