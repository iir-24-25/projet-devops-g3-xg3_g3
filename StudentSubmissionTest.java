package com.Gestion_Note.Note.Entities;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class StudentSubmissionTest {

    @Test
    public void testSettersAndGetters() {
        StudentSubmission submission = new StudentSubmission();

        Long id = 1L;
        Student student = new Student();
        Assignment assignment = new Assignment();
        byte[] fileData = {1, 2, 3};
        String fileName = "answer.pdf";
        String fileType = "application/pdf";
        Date date = new Date();
        Status status = Status.COMPLETED;

        submission.setId(id);
        submission.setStudent(student);
        submission.setAssignment(assignment);
        submission.setAnswerFileData(fileData);
        submission.setAnswerFileName(fileName);
        submission.setAnswerFileType(fileType);
        submission.setSubmittedAt(date);
        submission.setSubmissionStatus(status);

        assertEquals(id, submission.getId());
        assertEquals(student, submission.getStudent());
        assertEquals(assignment, submission.getAssignment());
        assertArrayEquals(fileData, submission.getAnswerFileData());
        assertEquals(fileName, submission.getAnswerFileName());
        assertEquals(fileType, submission.getAnswerFileType());
        assertEquals(date, submission.getSubmittedAt());
        assertEquals(status, submission.getSubmissionStatus());
    }

    @Test
    public void testDefaultConstructor() {
        StudentSubmission submission = new StudentSubmission();
        assertNull(submission.getId());
        assertNull(submission.getStudent());
        assertNull(submission.getAssignment());
        assertNull(submission.getAnswerFileData());
        assertNull(submission.getAnswerFileName());
        assertNull(submission.getAnswerFileType());
        assertNull(submission.getSubmittedAt());
        assertNull(submission.getSubmissionStatus());
    }
}
