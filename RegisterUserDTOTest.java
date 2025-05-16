package com.Gestion_Note.Note.DTO;

import com.Gestion_Note.Note.Entities.Roles;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterUserDTOTest {

    @Test
    public void testGettersAndSetters() {
        RegisterUserDTO dto = new RegisterUserDTO();

        dto.setName("Safaa");
        dto.setUsername("safaa123");
        dto.setEmail("safaa@example.com");
        dto.setPassword("pass123");
        dto.setRole(Roles.STUDENT);
        dto.setCne("CNE123");
        dto.setTeacherIdentificator("TID456");
        dto.setIdentificator("ADMIN789");

        assertEquals("Safaa", dto.getName());
        assertEquals("safaa123", dto.getUsername());
        assertEquals("safaa@example.com", dto.getEmail());
        assertEquals("pass123", dto.getPassword());
        assertEquals(Roles.STUDENT, dto.getRole());
        assertEquals("CNE123", dto.getCne());
        assertEquals("TID456", dto.getTeacherIdentificator());
        assertEquals("ADMIN789", dto.getIdentificator());
    }

    @Test
    public void testRoleSpecificFieldForStudentValid() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setRole(Roles.STUDENT);
        dto.setCne("CNE123");
        assertDoesNotThrow(dto::setRoleSpecificField);
    }

    @Test
    public void testRoleSpecificFieldForStudentMissingCNE() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setRole(Roles.STUDENT);
        dto.setCne("");
        Exception exception = assertThrows(IllegalArgumentException.class, dto::setRoleSpecificField);
        assertEquals("CNE is required for Student", exception.getMessage());
    }

    @Test
    public void testRoleSpecificFieldForTeacherValid() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setRole(Roles.TEACHER);
        dto.setTeacherIdentificator("T123");
        assertDoesNotThrow(dto::setRoleSpecificField);
    }

    @Test
    public void testRoleSpecificFieldForTeacherMissingIdentificator() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setRole(Roles.TEACHER);
        Exception exception = assertThrows(IllegalArgumentException.class, dto::setRoleSpecificField);
        assertEquals("Teacher Identificator is required for Teacher", exception.getMessage());
    }

    @Test
    public void testRoleSpecificFieldForAdminValid() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setRole(Roles.ADMIN);
        dto.setIdentificator("A123");
        assertDoesNotThrow(dto::setRoleSpecificField);
    }

    @Test
    public void testRoleSpecificFieldForAdminMissingIdentificator() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setRole(Roles.ADMIN);
        Exception exception = assertThrows(IllegalArgumentException.class, dto::setRoleSpecificField);
        assertEquals("Identificator is required for Admin", exception.getMessage());
    }
}
