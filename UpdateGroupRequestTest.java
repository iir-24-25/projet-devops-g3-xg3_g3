package com.Gestion_Note.Note.DTO;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateGroupRequestTest {

    @Test
    public void testGettersAndSetters() {
        UpdateGroupRequest request = new UpdateGroupRequest();

        Long groupId = 1L;
        String groupName = "Group A";
        List<String> teachers = Arrays.asList("teacher1", "teacher2");
        List<String> students = Arrays.asList("student1", "student2");

        request.setGroupId(groupId);
        request.setGroupName(groupName);
        request.setTeacherUsernames(teachers);
        request.setStudentUsernames(students);

        assertEquals(groupId, request.getGroupId());
        assertEquals(groupName, request.getGroupName());
        assertEquals(teachers, request.getTeacherUsernames());
        assertEquals(students, request.getStudentUsernames());
    }

    @Test
    public void testDefaultValues() {
        UpdateGroupRequest request = new UpdateGroupRequest();

        assertNull(request.getGroupId());
        assertNull(request.getGroupName());
        assertNull(request.getTeacherUsernames());
        assertNull(request.getStudentUsernames());
    }
}
