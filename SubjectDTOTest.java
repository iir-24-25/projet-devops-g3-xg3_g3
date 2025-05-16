package com.Gestion_Note.Note.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubjectDTOTest {

    @Test
    public void testNoArgsConstructor() {
        SubjectDTO subjectDTO = new SubjectDTO();
        assertNull(subjectDTO.getSubjectId());
        assertNull(subjectDTO.getName());
        assertNull(subjectDTO.getTeacher());
    }

    @Test
    public void testAllArgsConstructor() {
        SubjectDTO subjectDTO = new SubjectDTO(1L, "Mathematics", "Prof. Safaa");

        assertEquals(1L, subjectDTO.getSubjectId());
        assertEquals("Mathematics", subjectDTO.getName());
        assertEquals("Prof. Safaa", subjectDTO.getTeacher());
    }

    @Test
    public void testSettersAndGetters() {
        SubjectDTO subjectDTO = new SubjectDTO();

        subjectDTO.setSubjectId(2L);
        subjectDTO.setName("Physics");
        subjectDTO.setTeacher("Dr. Adnan");

        assertEquals(2L, subjectDTO.getSubjectId());
        assertEquals("Physics", subjectDTO.getName());
        assertEquals("Dr. Adnan", subjectDTO.getTeacher());
    }
}
