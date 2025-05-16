package com.Gestion_Note.Note.Entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Lob
    private byte[] fileData;

    private String fileName;
    private String fileType;

    @Enumerated(EnumType.STRING)
    private Status status = Status.TO_DO;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;

    private String teacherUsername;

    @Lob
    private byte[] submittedFileData;

    private String submittedFileName;
    private String submittedFileType;

    // Auto-set uploadedAt when persisted
    @PrePersist
    protected void onCreate() {
        this.uploadedAt = new Date();
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Date getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Date uploadedAt) { this.uploadedAt = uploadedAt; }

    public Date getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Date submittedAt) { this.submittedAt = submittedAt; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public String getTeacherUsername() { return teacherUsername; }
    public void setTeacherUsername(String teacherUsername) { this.teacherUsername = teacherUsername; }

    public byte[] getSubmittedFileData() { return submittedFileData; }
    public void setSubmittedFileData(byte[] submittedFileData) { this.submittedFileData = submittedFileData; }

    public String getSubmittedFileName() { return submittedFileName; }
    public void setSubmittedFileName(String submittedFileName) { this.submittedFileName = submittedFileName; }

    public String getSubmittedFileType() { return submittedFileType; }
    public void setSubmittedFileType(String submittedFileType) { this.submittedFileType = submittedFileType; }
}
