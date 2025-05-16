package com.Gestion_Note.Note.Entities;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentTest {

    @Test
    public void testGettersAndSetters() {
        Assignment assignment = new Assignment();

        Long id = 1L;
        String title = "Assignment Title";
        String description = "Assignment Description";
        byte[] fileData = "example".getBytes();
        String fileName = "file.pdf";
        String fileType = "application/pdf";
        Status status = Status.COMPLETED;
        Teacher teacher = new Teacher();
        Student student = new Student();
        Date uploadedAt = new Date();
        Date submittedAt = new Date();
        Date dueDate = new Date();
        String teacherUsername = "teacher1";
        byte[] submittedFileData = "submitted".getBytes();
        String submittedFileName = "submitted.pdf";
        String submittedFileType = "application/pdf";

        assignment.setId(id);
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setFileData(fileData);
        assignment.setFileName(fileName);
        assignment.setFileType(fileType);
        assignment.setStatus(status);
        assignment.setTeacher(teacher);
        assignment.setStudent(student);
        assignment.setUploadedAt(uploadedAt);
        assignment.setSubmittedAt(submittedAt);
        assignment.setDueDate(dueDate);
        assignment.setTeacherUsername(teacherUsername);
        assignment.setSubmittedFileData(submittedFileData);
        assignment.setSubmittedFileName(submittedFileName);
        assignment.setSubmittedFileType(submittedFileType);

        assertEquals(id, assignment.getId());
        assertEquals(title, assignment.getTitle());
        assertEquals(description, assignment.getDescription());
        assertArrayEquals(fileData, assignment.getFileData());
        assertEquals(fileName, assignment.getFileName());
        assertEquals(fileType, assignment.getFileType());
        assertEquals(status, assignment.getStatus());
        assertEquals(teacher, assignment.getTeacher());
        assertEquals(student, assignment.getStudent());
        assertEquals(uploadedAt, assignment.getUploadedAt());
        assertEquals(submittedAt, assignment.getSubmittedAt());
        assertEquals(dueDate, assignment.getDueDate());
        assertEquals(teacherUsername, assignment.getTeacherUsername());
        assertArrayEquals(submittedFileData, assignment.getSubmittedFileData());
        assertEquals(submittedFileName, assignment.getSubmittedFileName());
        assertEquals(submittedFileType, assignment.getSubmittedFileType());
    }

    @Test
    public void testOnCreateSetsUploadedAt() {
        Assignment assignment = new Assignment();

        assertNull(assignment.getUploadedAt());

        assignment.onCreate();

        assertNotNull(assignment.getUploadedAt());
        assertTrue(assignment.getUploadedAt().before(new Date(System.currentTimeMillis() + 1000)));
    }
}
