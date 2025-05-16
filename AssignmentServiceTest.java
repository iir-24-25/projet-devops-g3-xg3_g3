package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.Entities.*;
import com.Gestion_Note.Note.Repository.AssignmentRepository;
import com.Gestion_Note.Note.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AssignmentService assignmentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadAssignment_success() throws IOException {
        String teacherUsername = "teacher1";
        Teacher teacher = new Teacher();
        teacher.setUser_id(1L);
        teacher.setUsername(teacherUsername);

        when(userRepository.findByUsername(teacherUsername)).thenReturn(Optional.of(teacher));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test content".getBytes()
        );

        assignmentService.uploadAssignment(
                "Title",
                "Description",
                teacherUsername,
                file,
                new Date()
        );

        // Verify save called once
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
    }

    @Test
    void testUploadAssignment_userNotFound() {
        String teacherUsername = "unknown";

        when(userRepository.findByUsername(teacherUsername)).thenReturn(Optional.empty());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test content".getBytes()
        );

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                assignmentService.uploadAssignment("Title", "Description", teacherUsername, file, new Date()));

        assertEquals("Teacher not found", exception.getMessage());
    }

    @Test
    void testUploadAssignment_userNotTeacher() {
        String teacherUsername = "studentUser";
        User notTeacher = new User();
        notTeacher.setUsername(teacherUsername);
        notTeacher.setRole(Roles.STUDENT);

        when(userRepository.findByUsername(teacherUsername)).thenReturn(Optional.of(notTeacher));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test content".getBytes()
        );

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                assignmentService.uploadAssignment("Title", "Description", teacherUsername, file, new Date()));

        assertEquals("User is not a teacher", exception.getMessage());
    }

    @Test
    void testGetAllAssignments() {
        List<Assignment> assignments = List.of(new Assignment(), new Assignment());
        when(assignmentRepository.findAll()).thenReturn(assignments);

        List<Assignment> result = assignmentService.getAllAssignments();

        assertEquals(2, result.size());
        verify(assignmentRepository, times(1)).findAll();
    }

    @Test
    void testGetAssignmentById_found() {
        Assignment assignment = new Assignment();
        assignment.setTitle("Test");

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));

        Optional<Assignment> result = assignmentService.getAssignmentById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getTitle());
    }

    @Test
    void testGetAssignmentById_notFound() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Assignment> result = assignmentService.getAssignmentById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateAssignment_success() throws IOException {
        Assignment assignment = new Assignment();
        assignment.setTitle("Old Title");
        assignment.setTeacherUsername("oldTeacher");

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "newfile.txt",
                "text/plain",
                "Updated content".getBytes()
        );

        assignmentService.updateAssignment(1L, "New Title", "New Desc", "newTeacher", file, new Date());

        assertEquals("New Title", assignment.getTitle());
        assertEquals("newTeacher", assignment.getTeacherUsername());
        assertEquals("newfile.txt", assignment.getFileName());

        verify(assignmentRepository, times(1)).save(assignment);
    }

    @Test
    void testUpdateAssignment_notFound() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        IOException exception = assertThrows(EntityNotFoundException.class, () -> {
            assignmentService.updateAssignment(1L, "Title", null, null, null, null);
        });

        assertEquals("Assignment not found with ID: 1", exception.getMessage());
    }

    @Test
    void testDeleteAssignment_success() {
        Assignment assignment = new Assignment();
        assignment.setTitle("ToDelete");

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));

        assignmentService.deleteAssignment(1L);

        verify(assignmentRepository, times(1)).delete(assignment);
    }

    @Test
    void testDeleteAssignment_notFound() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            assignmentService.deleteAssignment(1L);
        });

        assertEquals("Assignment with id 1 not found", exception.getMessage());
    }
}
