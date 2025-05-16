package com.Gestion_Note.Note.DTO;

import com.Gestion_Note.Note.Entities.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegisterUserDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    private Roles role;

    private String cne;
    private String teacherIdentificator;
    private String identificator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getCne() {
        return cne;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }

    public String getTeacherIdentificator() {
        return teacherIdentificator;
    }

    public void setTeacherIdentificator(String teacherIdentificator) {
        this.teacherIdentificator = teacherIdentificator;
    }

    public String getIdentificator() {
        return identificator;
    }

    public void setIdentificator(String identificator) {
        this.identificator = identificator;
    }

    // Ensure that only the relevant field is set based on the role
    public void setRoleSpecificField() {
        switch (this.role) {
            case STUDENT:
                if (this.cne == null || this.cne.isEmpty()) {
                    throw new IllegalArgumentException("CNE is required for Student");
                }
                break;
            case TEACHER:
                if (this.teacherIdentificator == null || this.teacherIdentificator.isEmpty()) {
                    throw new IllegalArgumentException("Teacher Identificator is required for Teacher");
                }
                break;
            case ADMIN:
                if (this.identificator == null || this.identificator.isEmpty()) {
                    throw new IllegalArgumentException("Identificator is required for Admin");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid role");
        }
    }
}
