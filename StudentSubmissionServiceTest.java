package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.Entities.Assignment;
import com.Gestion_Note.Note.Entities.Status;
import com.Gestion_Note.Note.Entities.Student;
import com.Gestion_Note.Note.Repository.AssignmentRepository;
import com.Gestion_Note.Note.Repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentSubmissionServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentSubmissionService service;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private final String userEmail = "student@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock SecurityContextHolder to simulate logged-in user email
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void submitAssignment_onTime_success() throws Exception {
        Long assignmentId = 1L;

        Student student = new Student();
        student.setEmail(userEmail);

        Assignment assignment = new Assignment();
        assignment.setUploadedAt(new Date(System.currentTimeMillis() + 10000)); // Upload date in the future

        byte[] fileBytes = "file data".getBytes();

        when(studentRepository.findByEmail(userEmail)).thenReturn(Optional.of(student));
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        when(multipartFile.getBytes()).thenReturn(fileBytes);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(assignmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        String result = service.submitAssignment(assignmentId, multipartFile);

        assertEquals(Status.COMPLETED, assignment.getStatus());
        assertEquals(student, assignment.getStudent());
        assertEquals("test.txt", assignment.getFileName());
        assertArrayEquals(fileBytes, assignment.getFileData());
        assertEquals("Assignment submitted successfully on time.", result);
    }

    @Test
    void submitAssignment_lateSubmission_success() throws Exception {
        Long assignmentId = 1L;

        Student student = new Student();
        student.setEmail(userEmail);

        Assignment assignment = new Assignment();
        assignment.setUploadedAt(new Date(System.currentTimeMillis() - 10000)); // Upload date in the past

        when(studentRepository.findByEmail(userEmail)).thenReturn(Optional.of(student));
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        when(multipartFile.getBytes()).thenReturn("data".getBytes());
        when(multipartFile.getOriginalFilename()).thenReturn("file.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(assignmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        String result = service.submitAssignment(assignmentId, multipartFile);

        assertEquals(Status.LATE, assignment.getStatus());
        assertEquals("Assignment submitted, but it is late.", result);
    }

    @Test
    void submitAssignment_noUploadDate_success() throws Exception {
        Long assignmentId = 1L;

        Student student = new Student();
        student.setEmail(userEmail);

        Assignment assignment = new Assignment();
        assignment.setUploadedAt(null);

        when(studentRepository.findByEmail(userEmail)).thenReturn(Optional.of(student));
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        when(multipartFile.getBytes()).thenReturn("data".getBytes());
        when(multipartFile.getOriginalFilename()).thenReturn("file.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(assignmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        String result = service.submitAssignment(assignmentId, multipartFile);

        assertEquals(Status.COMPLETED, assignment.getStatus());
        assertEquals("Assignment submitted (upload date not set).", result);
    }

    @Test
    void submitAssignment_studentNotFound_throws() {
        when(studentRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            service.submitAssignment(1L, multipartFile);
        });

        assertTrue(ex.getMessage().contains("Student not found with email"));
    }

    @Test
    void submitAssignment_assignmentNotFound_throws() {
        Student student = new Student();
        student.setEmail(userEmail);

        when(studentRepository.findByEmail(userEmail)).thenReturn(Optional.of(student));
        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            service.submitAssignment(1L, multipartFile);
        });

        assertTrue(ex.getMessage().contains("Assignment not found"));
    }

    @Test
    void updateAssignment_success() throws Exception {
        Long assignmentId = 1L;

        Student student = new Student();
        student.setEmail(userEmail);

        Assignment assignment = new Assignment();
        assignment.setStudent(student);
        assignment.setUploadedAt(new Date(System.currentTimeMillis() + 10000));

        when(studentRepository.findByEmail(userEmail)).thenReturn(Optional.of(student));
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        when(multipartFile.getBytes()).thenReturn("update data".getBytes());
        when(multipartFile.getOriginalFilename()).thenReturn("update.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(assignmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        String result = service.updateAssignment(assignmentId, multipartFile);

        assertEquals(Status.COMPLETED, assignment.getStatus());
        assertEquals("Assignment updated successfully.", result);
    }

    @Test
    void updateAssignment_notSubmittedByStudent_throws() {
        Long assignmentId = 1L;

        Student student = new Student();
        student.setEmail(userEmail);

        Student otherStudent = new Student();
        otherStudent.setEmail("other@student.com");

        Assignment assignment = new Assignment();
        assignment.setStudent(otherStudent);

        when(studentRepository.findByEmail(userEmail)).thenReturn(Optional.of(student));
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            service.updateAssignment(assignmentId, multipartFile);
        });

        assertEquals("This assignment was not submitted by the logged-in student.", ex.getMessage());
    }

    @Test
    void deleteAssignment_success() throws Exception {
        Long assignmentId = 1L;

        Student student = new Student();
        student.setEmail(userEmail);

        Assignment assignment = new Assignment();
        assignment.setStudent(student);

        when(studentRepository.findByEmail(userEmail)).thenReturn(Optional.of(student));
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

        doNothing().when(assignmentRepository).delete(assignment);

        String result = service.deleteAssignment(assignmentId);

        verify(assignmentRepository).delete(assignment);
        assertEquals("Assignment submission deleted successfully.", result);
    }

    @Test
    void deleteAssignment_notSubmittedByStudent_throws() {
        Long assignmentId = 1L;

        Student student = new Student();
        student.setEmail(userEmail);

        Student otherStudent = new Student();
        otherStudent.setEmail("other@student.com");

        Assignment assignment = new Assignment();
        assignment.setStudent(otherStudent);

        when(studentRepository.findByEmail(userEmail)).thenReturn(Optional.of(student));
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            service.deleteAssignment(assignmentId);
        });

        assertEquals("This assignment was not submitted by the logged-in student.", ex.getMessage());
    }

    @Test
    void getAssignmentFile_success() throws Exception {
        Long assignmentId = 1L;

        byte[] fileData = "filecontent".getBytes();

        Assignment assignment = new Assignment();
        assignment.setSubmittedFileData(fileData);

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

        byte[] result = service.getAssignmentFile(assignmentId);

        assertArrayEquals(fileData, result);
    }

    @Test
    void getAssignmentFile_notFound_throws() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            service.getAssignmentFile(1L);
        });

        assertTrue(ex.getMessage().contains("Assignment not found"));
    }
}
