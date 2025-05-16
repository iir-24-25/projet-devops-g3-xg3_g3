package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.Entities.Assignment;
import com.Gestion_Note.Note.Entities.Grade;
import com.Gestion_Note.Note.Entities.Student;
import com.Gestion_Note.Note.Repository.AssignmentRepository;
import com.Gestion_Note.Note.Repository.GradeRepository;
import com.Gestion_Note.Note.Repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    public String assignGrade(Long assignmentId, String studentEmail, Double mark, String feedback) {
        // Find the assignment by ID
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with ID: " + assignmentId));

        Student student = studentRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with email: " + studentEmail));

        Grade grade = new Grade(assignment, student, mark, feedback);

        gradeRepository.save(grade);

        return "Grade assigned successfully.";
    }

    public Grade getGrade(Long assignmentId, String studentEmail) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with ID: " + assignmentId));

        Student student = studentRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with email: " + studentEmail));

        return gradeRepository.findByAssignmentAndStudent(assignment, student)
                .orElseThrow(() -> new EntityNotFoundException("Grade not found for the given student and assignment"));
    }
}
