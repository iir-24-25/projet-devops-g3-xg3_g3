package com.Gestion_Note.Note.Repository;

import com.Gestion_Note.Note.Entities.Assignment;
import com.Gestion_Note.Note.Entities.Grade;
import com.Gestion_Note.Note.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    Optional<Grade> findByAssignmentAndStudent(Assignment assignment, Student student);
}