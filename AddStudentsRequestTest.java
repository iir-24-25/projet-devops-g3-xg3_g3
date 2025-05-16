package com.Gestion_Note.Note.DTO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class AddStudentsRequestTest {

    @Test
    public void testGettersAndSetters() {
        AddStudentsRequest request = new AddStudentsRequest();

        Long groupId = 123L;
        List<String> students = Arrays.asList("student1", "student2", "student3");

        request.setGroupId(groupId);
        request.setStudentUsernames(students);

        assertEquals(groupId, request.getGroupId());
        assertEquals(students, request.getStudentUsernames());
    }
}
