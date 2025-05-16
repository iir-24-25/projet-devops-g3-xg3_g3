package com.Gestion_Note.Note.DTO;

import java.util.List;

public class GroupCreationRequest {
    private String groupName;
    private List<String> teacherUsernames;
    private List<String> studentUsernames;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

