package com.Gestion_Note.Note.Controllers;

import com.Gestion_Note.Note.Entities.Grade;
import com.Gestion_Note.Note.Services.GradeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GradeController.class)
public class GradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GradeService gradeService;

    @Test
    void testAssignGrade_success() throws Exception {
        Long assignmentId = 1L;
        String studentEmail = "student@example.com";
        Double mark = 15.5;
        String feedback = "Well done!";

        when(gradeService.assignGrade(assignmentId, studentEmail, mark, feedback))
                .thenReturn("Grade assigned successfully");

        mockMvc.perform(post("/grades/assign/{assignmentId}/{studentEmail}", assignmentId, studentEmail)
                        .param("mark", String.valueOf(mark))
                        .param("feedback", feedback))
                .andExpect(status().isOk())
                .andExpect(content().string("Grade assigned successfully"));
    }

    @Test
    void testViewGrade_success() throws Exception {
        Long assignmentId = 1L;
        String studentEmail = "student@example.com";

        Grade grade = new Grade();
        grade.setMark(17.0);
        grade.setFeedback("Great work!");

        when(gradeService.getGrade(assignmentId, studentEmail)).thenReturn(grade);

        mockMvc.perform(get("/grades/view/{assignmentId}/{studentEmail}", assignmentId, studentEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mark").value(17.0))
                .andExpect(jsonPath("$.feedback").value("Great work!"));
    }

    @Test
    void testAssignGrade_failure() throws Exception {
        when(gradeService.assignGrade(1L, "bad@student.com", 0.0, ""))
                .thenThrow(new RuntimeException("Invalid input"));

        mockMvc.perform(post("/grades/assign/1/bad@student.com")
                        .param("mark", "0.0")
                        .param("feedback", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Invalid input"));
    }

    @Test
    void testViewGrade_failure() throws Exception {
        when(gradeService.getGrade(999L, "missing@student.com"))
                .thenThrow(new RuntimeException("Grade not found"));

        mockMvc.perform(get("/grades/view/999/missing@student.com"))
                .andExpect(status().isBadRequest());
    }
}
