package com.Gestion_Note.Note.Repository;

import com.Gestion_Note.Note.Entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {


}
