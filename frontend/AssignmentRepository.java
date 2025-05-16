package com.Gestion_Note.Note.Repository;

import com.Gestion_Note.Note.Entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository <Assignment, Long> {
}
