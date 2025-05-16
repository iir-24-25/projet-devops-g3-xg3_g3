package com.Gestion_Note.Note.Controllers;

import com.Gestion_Note.Note.DTO.SubjectDTO;
import com.Gestion_Note.Note.Entities.Subject;
import com.Gestion_Note.Note.Entities.User;
import com.Gestion_Note.Note.Services.SubjectService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
public class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSaveSubject_success() throws Exception {
        SubjectDTO dto = new SubjectDTO();
        dto.setName("Math");

        doNothing().when(subjectService).addSubject(any(SubjectDTO.class));

        mockMvc.perform(post("/subject/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Subject saved"));
    }

    @Test
    public void testGetAllSubjects_success() throws Exception {
        Subject subject = new Subject();
        subject.setSubject_Id(1L);
        subject.setName("Math");
        User teacher = new User();
        teacher.setName("Safaa");
        subject.setTeacher(teacher);

        when(subjectService.getAllSubjects()).thenReturn(List.of(subject));

        mockMvc.perform(get("/subject/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subject_Id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Math"))
                .andExpect(jsonPath("$[0].teacher_username").value("Safaa"));
    }

    @Test
    public void testGetSubject_success() throws Exception {
        Subject subject = new Subject();
        subject.setSubject_Id(1L);
        subject.setName("Physics");
        User teacher = new User();
        teacher.setName("John Doe");
        subject.setTeacher(teacher);

        when(subjectService.getSubject(1L)).thenReturn(Optional.of(subject));

        mockMvc.perform(get("/subject/get").param("subject_Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject_Id").value(1))
                .andExpect(jsonPath("$.name").value("Physics"))
                .andExpect(jsonPath("$.teacherName").value("John Doe"));
    }

    @Test
    public void testUpdateSubject_success() throws Exception {
        Subject subject = new Subject();
        subject.setSubject_Id(1L);
        subject.setName("Biology");

        doNothing().when(subjectService).updateSubject(any(Subject.class));

        mockMvc.perform(put("/subject/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subject)))
                .andExpect(status().isOk())
                .andExpect(content().string("Subject Updated"));
    }

    @Test
    public void testDeleteSubject_success() throws Exception {
        doNothing().when(subjectService).deleteSubject(1L);

        mockMvc.perform(delete("/subject/delete")
                        .param("subject_Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("subject with the ID 1 deleted "));
    }

    @Test
    public void testGetSubject_notFound() throws Exception {
        when(subjectService.getSubject(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/subject/get").param("subject_Id", "99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Subject not found"));
    }
}
