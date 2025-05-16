package com.Gestion_Note.Note.Repository;


import com.Gestion_Note.Note.DTO.SubjectDTO;
import com.Gestion_Note.Note.Entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findAll();


}
