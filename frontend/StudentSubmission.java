package com.Gestion_Note.Note.Entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class StudentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Lob
    private byte[] answerFileData;

    private String answerFileName;

    private String answerFileType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;

    @Enumerated(EnumType.STRING)
    private Status submissionStatus; // Completed, Late, or other statuses

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public byte[] getAnswerFileData() {
        return answerFileData;
    }

    public void setAnswerFileData(byte[] answerFileData) {
        this.answerFileData = answerFileData;
    }

    public String getAnswerFileName() {
        return answerFileName;
    }

    public void setAnswerFileName(String answerFileName) {
        this.answerFileName = answerFileName;
    }

    public String getAnswerFileType() {
        return answerFileType;
    }

    public void setAnswerFileType(String answerFileType) {
        this.answerFileType = answerFileType;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Status getSubmissionStatus() {
        return submissionStatus;
    }

    public void setSubmissionStatus(Status submissionStatus) {
        this.submissionStatus = submissionStatus;
    }
}
