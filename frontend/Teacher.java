package com.Gestion_Note.Note.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
public class Teacher extends User {

    @ManyToMany(mappedBy = "responsibleTeachers")
    private List<Group> groups = new ArrayList<>();

}
