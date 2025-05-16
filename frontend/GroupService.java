package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.DTO.GroupCreationRequest;
import com.Gestion_Note.Note.Entities.*;
import com.Gestion_Note.Note.Repository.GroupRepository;
import com.Gestion_Note.Note.Repository.StudentRepository;
import com.Gestion_Note.Note.Repository.TeacherRepository;
import com.Gestion_Note.Note.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private final GroupRepository groupRepository;
    @Autowired
    private final TeacherRepository teacherRepository;
    @Autowired
    private final StudentRepository studentRepository;
    @Autowired
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, TeacherRepository teacherRepository, StudentRepository studentRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createGroup(GroupCreationRequest request) {
        Group group = new Group();
        group.setGroupName(request.getGroupName());

        // Setup teachers
        List<Teacher> validTeachers = new ArrayList<>();
        List<String> teacherUsernames = new ArrayList<>();
        for (String username : request.getTeacherUsernames()) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Teacher not found: " + username));
            if (user.getRole() != Roles.TEACHER) {
                throw new RuntimeException("User is not a teacher: " + username);
            }
            Teacher teacher = (Teacher) user;
            validTeachers.add(teacher);
            teacherUsernames.add(username);
        }
        group.setResponsibleTeachers(validTeachers);
        group.setTeacherUsernames(teacherUsernames);

        // Save the group FIRST! So it has an ID
        group = groupRepository.save(group);

        // Setup students
        List<Student> validStudents = new ArrayList<>();
        List<String> studentUsernames = new ArrayList<>();
        for (String username : request.getStudentUsernames()) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Student not found: " + username));
            if (user.getRole() != Roles.STUDENT) {
                throw new RuntimeException("User is not a student: " + username);
            }
            Student student = (Student) user;
            student.setGroup(group); // Set group to the student
            validStudents.add(student);
            studentUsernames.add(username);
        }

        group.setStudents(validStudents); // Update group-side reference to students
        group.setStudentUsernames(studentUsernames);

        // Save students and group together
        studentRepository.saveAll(validStudents); // Save all students first
        groupRepository.save(group); // Then save the group again to persist the changes
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroup(Long groupId) {
        Optional<Group> existingGroup = groupRepository.findById(groupId);
        if (existingGroup.isPresent()) {
            return existingGroup;
        } else {
            return Optional.empty();
        }
    }

    public void deleteGroup(Long groupId) {
        Optional<Group> existingGroup = groupRepository.findById(groupId);
        if (existingGroup.isPresent()) {
            groupRepository.delete(existingGroup.get());
        } else {
            throw new EntityNotFoundException("Group with id " + groupId + " not found");
        }
    }


    @Transactional
    public void updateGroup(Long groupId, String groupName, List<String> teacherUsernames, List<String> studentUsernames) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        group.setGroupName(groupName);

        List<Teacher> newTeachers = new ArrayList<>();
        for (String username : teacherUsernames) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + username));
            if (!(user instanceof Teacher)) {
                throw new IllegalArgumentException(username + " is not a teacher");
            }
            newTeachers.add((Teacher) user);
        }

        group.setResponsibleTeachers(newTeachers);

        List<Student> newStudents = new ArrayList<>();
        for (String username : studentUsernames) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("Student not found: " + username));
            if (!(user instanceof Student)) {
                throw new IllegalArgumentException(username + " is not a student");
            }
            Student student = (Student) user;
            student.setGroup(group);
            newStudents.add(student);
        }

        group.setStudents(newStudents);

        groupRepository.save(group);
    }

    @Transactional
    public void addStudentsToGroup(Long groupId, List<String> studentUsernames) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        for (String username : studentUsernames) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

            if (!(user instanceof Student)) {
                throw new IllegalArgumentException(username + " is not a student");
            }

            Student student = (Student) user;
            student.setGroup(group);
            group.getStudents().add(student);
        }

        groupRepository.save(group);
    }
}
