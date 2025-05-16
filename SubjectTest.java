package com.Gestion_Note.Note.Entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubjectTest {

    @Test
    public void testDefaultConstructor() {
        Subject subject = new Subject();

        assertNull(subject.getSubject_Id());
        assertNull(subject.getName());
        assertNull(subject.getTeacher());
        assertNull(subject.getTeacherUsername());
    }

    @Test
    public void testAllArgsConstructorAndGettersSetters() {
        Long id = 1L;
        String name = "Mathematics";
        String teacherUsername = "teacher123";

        User teacher = new User();
        teacher.setId(10L);
        teacher.setUsername(teacherUsername);

        Subject subject = new Subject(id, name, teacher, teacherUsername);

        assertEquals(id, subject.getSubject_Id());
        assertEquals(name, subject.getName());
        assertEquals(teacher, subject.getTeacher());
        assertEquals(teacherUsername, subject.getTeacherUsername());

        // Test setters
        subject.setName("Physics");
        assertEquals("Physics", subject.getName());
    }

    @Test
    public void testSettersIndependently() {
        Subject subject = new Subject();

        Long id = 2L;
        String name = "Biology";
        String teacherUsername = "bioTeacher";
        User teacher = new User();
        teacher.setUsername(teacherUsername);

        subject.setSubject_Id(id);
        subject.setName(name);
        subject.setTeacher(teacher);
        subject.setTeacherUsername(teacherUsername);

        assertEquals(id, subject.getSubject_Id());
        assertEquals(name, subject.getName());
        assertEquals(teacher, subject.getTeacher());
        assertEquals(teacherUsername, subject.getTeacherUsername());
    }
}
