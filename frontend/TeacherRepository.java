package com.Gestion_Note.Note.Repository;

import com.Gestion_Note.Note.Entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

}
