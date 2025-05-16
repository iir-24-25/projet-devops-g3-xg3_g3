package com.Gestion_Note.Note.Controllers;

import com.Gestion_Note.Note.Entities.Assignment;
import com.Gestion_Note.Note.Services.AssignmentService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssignmentControllerTest {

    @InjectMocks
    private AssignmentController assignmentController;

    @Mock
    private AssignmentService assignmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadAssignment_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "Dummy content".getBytes());

        doNothing().when(assignmentService).uploadAssignment(anyString(), anyString(), anyString(), any(MultipartFile.class), any());

        ResponseEntity<String> response = assignmentController.uploadAssignment("Test Title", "Desc", "teacher1", file, new Date());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Assignment uploaded successfully.", response.getBody());

        verify(assignmentService, times(1)).uploadAssignment(anyString(), anyString(), anyString(), any(MultipartFile.class), any());
    }

    @Test
    public void testUploadAssignment_Failure() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "Dummy content".getBytes());

        doThrow(new RuntimeException("Upload failed")).when(assignmentService).uploadAssignment(anyString(), anyString(), anyString(), any(MultipartFile.class), any());

        ResponseEntity<String> response = assignmentController.uploadAssignment("Test Title", "Desc", "teacher1", file, new Date());

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Error uploading assignment"));
    }

    @Test
    public void testUpdateAssignment_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "Dummy content".getBytes());

        doNothing().when(assignmentService).updateAssignment(anyLong(), anyString(), anyString(), anyString(), any(), any());

        ResponseEntity<String> response = assignmentController.updateAssignment(1L, "New Title", "New Desc", "teacher1", file, new Date());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Assignment updated successfully.", response.getBody());

        verify(assignmentService, times(1)).updateAssignment(anyLong(), anyString(), anyString(), anyString(), any(), any());
    }

    @Test
    public void testUpdateAssignment_EntityNotFound() throws IOException {
        doThrow(new EntityNotFoundException("Not found")).when(assignmentService).updateAssignment(anyLong(), any(), any(), any(), any(), any());

        ResponseEntity<String> response = assignmentController.updateAssignment(1L, null, null, null, null, null);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetAllAssignments() {
        List<Assignment> assignments = Arrays.asList(new Assignment(), new Assignment());
        when(assignmentService.getAllAssignments()).thenReturn(assignments);

        List<Assignment> result = assignmentController.getAllAssignments();

        assertEquals(2, result.size());
        verify(assignmentService, times(1)).getAllAssignments();
    }

    @Test
    public void testGetAssignmentById_Found() {
        Assignment assignment = new Assignment();
        assignment.setId(1L);
        when(assignmentService.getAssignmentById(1L)).thenReturn(Optional.of(assignment));

        ResponseEntity<Assignment> response = assignmentController.getAssignmentById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(assignment, response.getBody());
    }

    @Test
    public void testGetAssignmentById_NotFound() {
        when(assignmentService.getAssignmentById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Assignment> response = assignmentController.getAssignmentById(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteAssignment_Success() {
        doNothing().when(assignmentService).deleteAssignment(1L);

        ResponseEntity<String> response = assignmentController.deleteAssignment(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Assignment deleted successfully.", response.getBody());
    }

    @Test
    public void testDeleteAssignment_NotFound() {
        doThrow(new EntityNotFoundException("Not found")).when(assignmentService).deleteAssignment(1L);

        ResponseEntity<String> response = assignmentController.deleteAssignment(1L);

        assertEquals(404, response.getStatusCodeValue());
    }
}
