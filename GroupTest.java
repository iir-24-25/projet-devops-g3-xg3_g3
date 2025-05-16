package com.Gestion_Note.Note.Entities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GroupTest {

    @Test
    public void testDefaultValues() {
        Group group = new Group();

        assertNull(group.getGroupId());
        assertNull(group.getGroupName());

        assertNotNull(group.getTeacherUsernames());
        assertTrue(group.getTeacherUsernames().isEmpty());

        assertNotNull(group.getStudentUsernames());
        assertTrue(group.getStudentUsernames().isEmpty());

        assertNotNull(group.getResponsibleTeachers());
        assertTrue(group.getResponsibleTeachers().isEmpty());

        assertNotNull(group.getStudents());
        assertTrue(group.getStudents().isEmpty());
    }

    @Test
    public void testSettersAndGetters() {
        Group group = new Group();

        Long id = 1L;
        String name = "Groupe A";
        List<String> teacherUsernames = Arrays.asList("teacher1", "teacher2");
        List<String> studentUsernames = Arrays.asList("student1", "student2");

        Teacher t1 = new Teacher();
        Teacher t2 = new Teacher();
        List<Teacher> teachers = Arrays.asList(t1, t2);

        Student s1 = new Student();
        Student s2 = new Student();
        List<Student> students = Arrays.asList(s1, s2);

        group.setGroupId(id);
        group.setGroupName(name);
        group.setTeacherUsernames(teacherUsernames);
        group.setStudentUsernames(studentUsernames);
        group.setResponsibleTeachers(teachers);
        group.setStudents(students);

        assertEquals(id, group.getGroupId());
        assertEquals(name, group.getGroupName());
        assertEquals(teacherUsernames, group.getTeacherUsernames());
        assertEquals(studentUsernames, group.getStudentUsernames());
        assertEquals(teachers, group.getResponsibleTeachers());
        assertEquals(students, group.getStudents());
    }
}
