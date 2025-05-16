package com.Gestion_Note.Note.Controllers;

import com.Gestion_Note.Note.Entities.Assignment;
import com.Gestion_Note.Note.Services.AssignmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAssignment(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String teacherUsername,
            @RequestParam("file") MultipartFile file,
            @RequestParam("dueDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dueDate
    ) {
        try {
            assignmentService.uploadAssignment(title, description, teacherUsername, file, dueDate);
            return ResponseEntity.ok("Assignment uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading assignment: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAssignment(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String teacherUsername,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "dueDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dueDate
    ) {
        try {
            assignmentService.updateAssignment(id, title, description, teacherUsername, file, dueDate);
            return ResponseEntity.ok("Assignment updated successfully.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error updating file: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        Optional<Assignment> assignment = assignmentService.getAssignmentById(id);
        return assignment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAssignment(@PathVariable Long id) {
        try {
            assignmentService.deleteAssignment(id);
            return ResponseEntity.ok("Assignment deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
