package com.Gestion_Note.Note.Entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GradeTest {

    @Test
    public void testDefaultConstructor() {
        Grade grade = new Grade();

        assertNull(grade.getId());
        assertNull(grade.getAssignment());
        assertNull(grade.getStudent());
        assertNull(grade.getMark());
        assertNull(grade.getFeedback());
        assertFalse(grade.isGraded());
    }

    @Test
    public void testParameterizedConstructor() {
        Assignment assignment = new Assignment();
        Student student = new Student();
        Double mark = 17.5;
        String feedback = "Good job";

        Grade grade = new Grade(assignment, student, mark, feedback);

        assertEquals(assignment, grade.getAssignment());
        assertEquals(student, grade.getStudent());
        assertEquals(mark, grade.getMark());
        assertEquals(feedback, grade.getFeedback());
        assertTrue(grade.isGraded());
    }

    @Test
    public void testGettersAndSetters() {
        Grade grade = new Grade();

        Long id = 10L;
        Assignment assignment = new Assignment();
        Student student = new Student();
        Double mark = 12.0;
        String feedback = "Needs improvement";

        grade.setId(id);
        grade.setAssignment(assignment);
        grade.setStudent(student);
        grade.setMark(mark);
        grade.setFeedback(feedback);
        grade.setGraded(true);

        assertEquals(id, grade.getId());
        assertEquals(assignment, grade.getAssignment());
        assertEquals(student, grade.getStudent());
        assertEquals(mark, grade.getMark());
        assertEquals(feedback, grade.getFeedback());
        assertTrue(grade.isGraded());
    }
}
