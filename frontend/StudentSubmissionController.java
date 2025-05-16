package com.Gestion_Note.Note.Controllers;

import com.Gestion_Note.Note.Services.StudentSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/student-submissions")
public class StudentSubmissionController {

    @Autowired
    private StudentSubmissionService submissionService;

    @PostMapping("/submit/{assignmentId}")
    public ResponseEntity<String> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("file") MultipartFile file) {
        try {
            String message = submissionService.submitAssignment(assignmentId, file);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{assignmentId}")
    public ResponseEntity<String> updateAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("file") MultipartFile file) {
        try {
            String message = submissionService.updateAssignment(assignmentId, file);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(@PathVariable Long assignmentId) {
        try {
            String message = submissionService.deleteAssignment(assignmentId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/download/{assignmentId}")
    public ResponseEntity<byte[]> downloadAssignment(@PathVariable Long assignmentId) {
        try {
            byte[] fileData = submissionService.getAssignmentFile(assignmentId);

            String filename = "assignment_" + assignmentId + ".pdf";
            String contentType = "application/pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileData.length));

            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
