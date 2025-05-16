package com.Gestion_Note.Note.Controllers;


import com.Gestion_Note.Note.DTO.SubjectDTO;
import com.Gestion_Note.Note.Entities.Subject;
import com.Gestion_Note.Note.Services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<String> saveSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            subjectService.addSubject(subjectDTO);
            return ResponseEntity.ok("Subject saved");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /*
    @GetMapping("/get/all")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        try {
            List<Subject> subjects = subjectService.getAllSubjects();
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<Subject>> getSubjectByTeacher(@RequestParam String teacherUsername) {
        try {
            User teacher = userRepository.findByUsername(teacherUsername)
                    .orElseThrow(() -> new RuntimeException("Teacher not found with username: " + teacherUsername));
            List<Subject> subjects = subjectService.getSubjectByTeacher(teacher);
            if (subjects.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(subjects);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }*/

    @PreAuthorize("permitAll()")
    @PutMapping("/update")
    public ResponseEntity<?> updateSubject(@RequestBody Subject subject) {
        try {
            subjectService.updateSubject(subject);
            return ResponseEntity.ok("Subject Updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Subject Not Found");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteSubject(@RequestParam Long subject_Id) {
        try {
            subjectService.deleteSubject(subject_Id);
            return ResponseEntity.ok("subject with the ID " + subject_Id + " deleted ");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Subject Not Found with the ID " + subject_Id);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @GetMapping("/get/all")
    public ResponseEntity<?> getAllSubjects() {
        try {
            List<Subject> subjects = subjectService.getAllSubjects();
            List<Map<String, Object>> result = new ArrayList<>();
            for (Subject subject : subjects) {
                Map<String, Object> data = new HashMap<>();
                data.put("subject_Id", subject.getSubject_Id());
                data.put("name", subject.getName());
                data.put("teacher_username", subject.getTeacher() != null ? subject.getTeacher().getName() : null);
                result.add(data);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @GetMapping("/get")
    public ResponseEntity<?> getSubject(@RequestParam Long subject_Id) {
        try {
            System.out.println("Received request to get subject with ID: " + subject_Id);

            Optional<Subject> subjectOpt = subjectService.getSubject(subject_Id);

            if (subjectOpt.isPresent()) {
                Subject subject = subjectOpt.get();

                Map<String, Object> result = new HashMap<>();
                result.put("subject_Id", subject.getSubject_Id());
                result.put("name", subject.getName());
                result.put("teacherName", subject.getTeacher() != null ? subject.getTeacher().getName() : null);

                return ResponseEntity.ok(result);
            } else {
                System.out.println("Subject not found for ID: " + subject_Id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subject not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the subject: " + e.getMessage());
        }
    }

}