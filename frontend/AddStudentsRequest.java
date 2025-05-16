package com.Gestion_Note.Note.DTO;

import java.util.List;

public class AddStudentsRequest {
    private Long groupId;
    private List<String> studentUsernames;

    // Getters and setters
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<String> getStudentUsernames() {
        return studentUsernames;
    }

    public void setStudentUsernames(List<String> studentUsernames) {
        this.studentUsernames = studentUsernames;
    }
}

