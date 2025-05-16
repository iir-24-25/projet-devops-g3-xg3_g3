package com.Gestion_Note.Note.Controllers;

import com.Gestion_Note.Note.Services.StudentSubmissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentSubmissionController.class)
public class StudentSubmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentSubmissionService submissionService;

    @Test
    void testSubmitAssignment_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "test content".getBytes());

        when(submissionService.submitAssignment(eq(1L), any())).thenReturn("Assignment submitted");

        mockMvc.perform(multipart("/student-submissions/submit/1")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Assignment submitted"));
    }

    @Test
    void testUpdateAssignment_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "update.pdf", MediaType.APPLICATION_PDF_VALUE, "updated content".getBytes());

        when(submissionService.updateAssignment(eq(2L), any())).thenReturn("Assignment updated");

        mockMvc.perform(multipart("/student-submissions/update/2")
                        .file(file)
                        .with(request -> { request.setMethod("PUT"); return request; }))
                .andExpect(status().isOk())
                .andExpect(content().string("Assignment updated"));
    }

    @Test
    void testDeleteAssignment_success() throws Exception {
        when(submissionService.deleteAssignment(3L)).thenReturn("Assignment deleted");

        mockMvc.perform(delete("/student-submissions/delete/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Assignment deleted"));
    }

    @Test
    void testDownloadAssignment_success() throws Exception {
        byte[] fileContent = "PDF FILE CONTENT".getBytes();

        when(submissionService.getAssignmentFile(4L)).thenReturn(fileContent);

        mockMvc.perform(get("/student-submissions/download/4"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=assignment_4.pdf"))
                .andExpect(header().string("Content-Type", "application/pdf"))
                .andExpect(content().bytes(fileContent));
    }

    @Test
    void testDownloadAssignment_error() throws Exception {
        when(submissionService.getAssignmentFile(5L)).thenThrow(new IOException("File not found"));

        mockMvc.perform(get("/student-submissions/download/5"))
                .andExpect(status().isInternalServerError());
    }
}
