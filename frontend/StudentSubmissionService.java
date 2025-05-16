package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.Entities.Assignment;
import com.Gestion_Note.Note.Entities.Status;
import com.Gestion_Note.Note.Entities.Student;
import com.Gestion_Note.Note.Repository.AssignmentRepository;
import com.Gestion_Note.Note.Repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
public class StudentSubmissionService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    public String submitAssignment(Long assignmentId, MultipartFile file) throws Exception {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with email: " + email));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with ID: " + assignmentId));

        assignment.setStudent(student);
        assignment.setSubmittedAt(new Date());
        assignment.setFileData(file.getBytes());
        assignment.setFileName(file.getOriginalFilename());
        assignment.setFileType(file.getContentType());

        if (assignment.getUploadedAt() != null) {
            if (assignment.getSubmittedAt().before(assignment.getUploadedAt())) {
                assignment.setStatus(Status.COMPLETED);
                assignmentRepository.save(assignment);
                return "Assignment submitted successfully on time.";
            } else {
                assignment.setStatus(Status.LATE);
                assignmentRepository.save(assignment);
                return "Assignment submitted, but it is late.";
            }
        } else {
            assignment.setStatus(Status.COMPLETED);
            assignmentRepository.save(assignment);
            return "Assignment submitted (upload date not set).";
        }
    }

    public String updateAssignment(Long assignmentId, MultipartFile file) throws Exception {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with email: " + email));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with ID: " + assignmentId));

        if (assignment.getStudent() != null && !assignment.getStudent().equals(student)) {
            throw new IllegalStateException("This assignment was not submitted by the logged-in student.");
        }

        assignment.setSubmittedAt(new Date());
        assignment.setFileData(file.getBytes());
        assignment.setFileName(file.getOriginalFilename());
        assignment.setFileType(file.getContentType());

        if (assignment.getUploadedAt() != null) {
            if (assignment.getSubmittedAt().before(assignment.getUploadedAt())) {
                assignment.setStatus(Status.COMPLETED);
            } else {
                assignment.setStatus(Status.LATE);
            }
        } else {
            assignment.setStatus(Status.COMPLETED);
        }

        assignmentRepository.save(assignment);
        return "Assignment updated successfully.";
    }

    public String deleteAssignment(Long assignmentId) throws Exception {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with email: " + email));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with ID: " + assignmentId));

        if (assignment.getStudent() == null || !assignment.getStudent().equals(student)) {
            throw new IllegalStateException("This assignment was not submitted by the logged-in student.");
        }

        assignmentRepository.delete(assignment);
        return "Assignment submission deleted successfully.";
    }

    public byte[] getAssignmentFile(Long assignmentId) throws IOException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with ID: " + assignmentId));

        return assignment.getSubmittedFileData();
    }
}
