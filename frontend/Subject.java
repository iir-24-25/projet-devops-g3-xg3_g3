package com.Gestion_Note.Note.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subject_Id;

    private String name;
    @Column(name = "teacher_username")
    @JsonBackReference
    private String teacherUsername;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User teacher;

    public Subject() {
    }

    public Subject(Long subject_Id, String name, User teacher, String teacherUsername) {
        this.subject_Id = subject_Id;
        this.name = name;
        this.teacher = teacher;
        this.teacherUsername = teacherUsername;
    }

    public Long getSubject_Id() {
        return subject_Id;
    }

    public void setSubject_Id(Long subject_Id) {
        this.subject_Id = subject_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public String getTeacherUsername() {
        return teacherUsername;
    }

    public void setTeacherUsername(String teacherUsername) {
        this.teacherUsername = teacherUsername;
    }
}
