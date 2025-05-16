package com.Gestion_Note.Note.Controllers;

import com.Gestion_Note.Note.Services.GradeService;
import com.Gestion_Note.Note.Entities.Grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @PostMapping("/assign/{assignmentId}/{studentEmail}")
    public ResponseEntity<String> assignGrade(
            @PathVariable Long assignmentId,
            @PathVariable String studentEmail,
            @RequestParam Double mark,
            @RequestParam String feedback) {
        try {
            String message = gradeService.assignGrade(assignmentId, studentEmail, mark, feedback);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/view/{assignmentId}/{studentEmail}")
    public ResponseEntity<Grade> viewGrade(
            @PathVariable Long assignmentId,
            @PathVariable String studentEmail) {
        try {
            Grade grade = gradeService.getGrade(assignmentId, studentEmail);
            return ResponseEntity.ok(grade);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
