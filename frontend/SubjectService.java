package com.Gestion_Note.Note.Services;


import com.Gestion_Note.Note.DTO.SubjectDTO;
import com.Gestion_Note.Note.Entities.Roles;
import com.Gestion_Note.Note.Entities.Subject;
import com.Gestion_Note.Note.Entities.User;
import com.Gestion_Note.Note.Repository.SubjectRepository;
import com.Gestion_Note.Note.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public SubjectService(SubjectRepository subjectRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    public String addSubject(SubjectDTO subjectDTO) {
        User teacher = userRepository.findByUsername(subjectDTO.getTeacher())
                .orElseThrow(() -> new RuntimeException("Teacher not found with username: " + subjectDTO.getTeacher()));
        if (!teacher.getRole().equals(Roles.TEACHER)) {
            throw new RuntimeException("The user does not have the teacher role");
        }
        Subject subject = new Subject();
        subject.setName(subjectDTO.getName());
        subject.setTeacher(teacher);
        subject.setTeacherUsername(teacher.getUsername());
        subjectRepository.save(subject);

        return "Subject saved successfully";
    }
/*
    public List<Subject> getAllSubjects (){
        List <Subject> subjects = subjectRepository.findAll();
        return subjects;
    }

    public List<Subject> getSubjectByTeacher(User teacher) {
        List<Subject> subjects = subjectRepository.findByTeacherUsername(teacher.getUsername());
        return subjects;
    }*/


    public Subject updateSubject(Subject subject) {
        Optional<Subject> existingSubjectOptional = subjectRepository.findById(subject.getSubject_Id());

        if (existingSubjectOptional.isPresent()) {
            Subject existingSubject = existingSubjectOptional.get();
            existingSubject.setName(subject.getName());
            existingSubject.setTeacherUsername(subject.getTeacherUsername());
            return subjectRepository.save(existingSubject);
        } else {
            throw new RuntimeException("Subject Not Found");
        }
    }


    public void deleteSubject (Long subject_Id){
        Optional <Subject> existingSubject = subjectRepository.findById(subject_Id);
        existingSubject.ifPresent(subjectRepository::delete);
    }

    public List <Subject> getAllSubjects(){
        List <Subject> existingSubject = subjectRepository.findAll();
        return existingSubject;
    }

    public Optional<Subject> getSubject(Long subject_Id){
        Optional<Subject> existingSubject = subjectRepository.findById(subject_Id);
        if (existingSubject.isPresent()){
            return existingSubject;
        }else {
            return Optional.empty();
        }
    }
}
