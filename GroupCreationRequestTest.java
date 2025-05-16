package com.Gestion_Note.Note.DTO;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GroupCreationRequestTest {

    @Test
    public void testGettersAndSetters() {
        GroupCreationRequest request = new GroupCreationRequest();

        String groupName = "Group A";
        List<String> teachers = Arrays.asList("teacher1", "teacher2");
        List<String> students = Arrays.asList("student1", "student2", "student3");

        request.setGroupName(groupName);
        request.setTeacherUsernames(teachers);
        request.setStudentUsernames(students);

        assertEquals(groupName, request.getGroupName());
        assertEquals(teachers, request.getTeacherUsernames());
        assertEquals(students, request.getStudentUsernames());
    }
}
