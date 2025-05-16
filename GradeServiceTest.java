package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.Entities.Assignment;
import com.Gestion_Note.Note.Entities.Grade;
import com.Gestion_Note.Note.Entities.Student;
import com.Gestion_Note.Note.Repository.AssignmentRepository;
import com.Gestion_Note.Note.Repository.GradeRepository;
import com.Gestion_Note.Note.Repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private GradeService gradeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void assignGrade_success() {
        Long assignmentId = 1L;
        String studentEmail = "student@example.com";
        Double mark = 85.0;
        String feedback = "Good job";

        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);

        Student student = new Student();
        student.setEmail(studentEmail);

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        when(studentRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));
        when(gradeRepository.save(any(Grade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = gradeService.assignGrade(assignmentId, studentEmail, mark, feedback);

        assertEquals("Grade assigned successfully.", result);
        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void assignGrade_assignmentNotFound_throws() {
        Long assignmentId = 1L;
        String studentEmail = "student@example.com";

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> gradeService.assignGrade(assignmentId, studentEmail, 90.0, "Well done"));

        assertTrue(ex.getMessage().contains("Assignment not found"));
    }

    @Test
    void assignGrade_studentNotFound_throws() {
        Long assignmentId = 1L;
        String studentEmail = "student@example.com";

        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        when(studentRepository.findByEmail(studentEmail)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> gradeService.assignGrade(assignmentId, studentEmail, 90.0, "Well done"));

        assertTrue(ex.getMessage().contains("Student not found"));
    }

    @Test
    void getGrade_success() {
        Long assignmentId = 1L;
        String studentEmail = "student@example.com";

        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);

        Student student = new Student();
        student.setEmail(studentEmail);

        Grade grade = new Grade(assignment, student, 88.0, "Good");

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        when(studentRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));
        when(gradeRepository.findByAssignmentAndStudent(assignment, student)).thenReturn(Optional.of(grade));

        Grade result = gradeService.getGrade(assignmentId, studentEmail);

        assertNotNull(result);
        assertEquals(88.0, result.getMark());
        assertEquals("Good", result.getFeedback());
    }

    @Test
    void getGrade_gradeNotFound_throws() {
        Long assignmentId = 1L;
        String studentEmail = "student@example.com";

        Assignment assignment = new Assignment();
        Student student = new Student();

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        when(studentRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));
        when(gradeRepository.findByAssignmentAndStudent(assignment, student)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> gradeService.getGrade(assignmentId, studentEmail));

        assertTrue(ex.getMessage().contains("Grade not found"));
    }
}
