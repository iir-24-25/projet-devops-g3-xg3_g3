package com.Gestion_Note.Note.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "`groups`")
@JsonIgnoreProperties({"students", "responsibleTeachers"})
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    private String groupName;
    @ElementCollection
    private List<String> teacherUsernames = new ArrayList<>();

    @ElementCollection
    private List<String> studentUsernames = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "group_teacher",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Teacher> responsibleTeachers = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Student> students = new ArrayList<>();

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Teacher> getResponsibleTeachers() {
        return responsibleTeachers;
    }

    public void setResponsibleTeachers(List<Teacher> responsibleTeachers) {
        this.responsibleTeachers = responsibleTeachers;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<String> getTeacherUsernames() {
        return teacherUsernames;
    }

    public void setTeacherUsernames(List<String> teacherUsernames) {
        this.teacherUsernames = teacherUsernames;
    }

    public List<String> getStudentUsernames() {
        return studentUsernames;
    }

    public void setStudentUsernames(List<String> studentUsernames) {
        this.studentUsernames = studentUsernames;
    }
}
