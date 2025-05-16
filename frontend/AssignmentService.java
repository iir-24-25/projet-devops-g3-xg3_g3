package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.Entities.*;
import com.Gestion_Note.Note.Repository.AssignmentRepository;
import com.Gestion_Note.Note.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
    }

    public void uploadAssignment(String title, String description, String teacherUsername, MultipartFile file, Date dueDate) {
        // Retrieve teacher from database
        User teacherUser = userRepository.findByUsername(teacherUsername)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (!(teacherUser instanceof Teacher)) {
            throw new RuntimeException("User is not a teacher");
        }

        Teacher teacher = (Teacher) teacherUser;

        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setTeacher(teacher);
        assignment.setTeacherUsername(teacherUsername);
        assignment.setDueDate(dueDate);
        assignment.setStatus(Status.TO_DO);

        try {
            assignment.setFileName(file.getOriginalFilename());
            assignment.setFileType(file.getContentType());
            assignment.setFileData(file.getBytes());

            assignmentRepository.save(assignment);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Optional<Assignment> getAssignmentById(Long assignmentId) {
        return assignmentRepository.findById(assignmentId);
    }

    public void updateAssignment(Long id, String title, String description, String teacherUsername, MultipartFile file, Date dueDate) throws IOException {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with ID: " + id));

        if (title != null && !title.isEmpty()) {
            assignment.setTitle(title);
        }
        if (description != null && !description.isEmpty()) {
            assignment.setDescription(description);
        }
        if (teacherUsername != null && !teacherUsername.isEmpty()) {
            assignment.setTeacherUsername(teacherUsername);
        }
        if (dueDate != null) {
            assignment.setDueDate(dueDate);
        }

        if (file != null && !file.isEmpty()) {
            assignment.setFileData(file.getBytes());
            assignment.setFileName(file.getOriginalFilename());
            assignment.setFileType(file.getContentType());
        }

        assignmentRepository.save(assignment);
    }

    public void deleteAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assignment with id " + id + " not found"));

        assignmentRepository.delete(assignment);
    }
}
