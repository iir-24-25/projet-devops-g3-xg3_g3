package com.Gestion_Note.Note.DTO;

public class SubjectDTO {
    private Long subjectId;
    private String name;
    private String teacher;

    public SubjectDTO() {}

    public SubjectDTO(Long subjectId, String name, String teacher) {
        this.subjectId = subjectId;
        this.name = name;
        this.teacher = teacher;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
