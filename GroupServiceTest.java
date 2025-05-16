package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.DTO.GroupCreationRequest;
import com.Gestion_Note.Note.Entities.*;
import com.Gestion_Note.Note.Repository.GroupRepository;
import com.Gestion_Note.Note.Repository.StudentRepository;
import com.Gestion_Note.Note.Repository.TeacherRepository;
import com.Gestion_Note.Note.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGroup_success() {
        GroupCreationRequest request = new GroupCreationRequest();
        request.setGroupName("Groupe A");
        request.setTeacherUsernames(List.of("teacher1"));
        request.setStudentUsernames(List.of("student1"));

        Teacher teacher = new Teacher();
        teacher.setUsername("teacher1");
        teacher.setRole(Roles.TEACHER);

        Student student = new Student();
        student.setUsername("student1");
        student.setRole(Roles.STUDENT);

        when(userRepository.findByUsername("teacher1")).thenReturn(Optional.of(teacher));
        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(student));
        when(groupRepository.save(any(Group.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(studentRepository.saveAll(anyList())).thenReturn(List.of(student));

        Group result = groupService.createGroup(request);

        assertEquals("Groupe A", result.getGroupName());
        assertTrue(result.getResponsibleTeachers().contains(teacher));
        assertTrue(result.getStudents().contains(student));
        assertEquals(result, student.getGroup());

        verify(groupRepository, times(2)).save(any(Group.class));
        verify(studentRepository).saveAll(anyList());
    }

    @Test
    void createGroup_teacherNotFound_throws() {
        GroupCreationRequest request = new GroupCreationRequest();
        request.setGroupName("Group B");
        request.setTeacherUsernames(List.of("unknownTeacher"));
        request.setStudentUsernames(List.of());

        when(userRepository.findByUsername("unknownTeacher")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            groupService.createGroup(request);
        });

        assertEquals("Teacher not found: unknownTeacher", ex.getMessage());
    }

    @Test
    void createGroup_userIsNotTeacher_throws() {
        GroupCreationRequest request = new GroupCreationRequest();
        request.setGroupName("Group C");
        request.setTeacherUsernames(List.of("notATeacher"));
        request.setStudentUsernames(List.of());

        User user = new User();
        user.setUsername("notATeacher");
        user.setRole(Roles.STUDENT); // Wrong role

        when(userRepository.findByUsername("notATeacher")).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            groupService.createGroup(request);
        });

        assertEquals("notATeacher is not a teacher", ex.getMessage());
    }

    @Test
    void updateGroup_success() {
        Long groupId = 1L;
        Group group = new Group();
        group.setGroupName("Old Group");
        group.setStudents(new ArrayList<>());
        group.setResponsibleTeachers(new ArrayList<>());

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        Teacher teacher = new Teacher();
        teacher.setUsername("teacher1");
        teacher.setRole(Roles.TEACHER);

        Student student = new Student();
        student.setUsername("student1");
        student.setRole(Roles.STUDENT);

        when(userRepository.findByUsername("teacher1")).thenReturn(Optional.of(teacher));
        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(student));

        groupService.updateGroup(groupId, "New Group", List.of("teacher1"), List.of("student1"));

        assertEquals("New Group", group.getGroupName());
        assertTrue(group.getResponsibleTeachers().contains(teacher));
        assertTrue(group.getStudents().contains(student));
        assertEquals(group, student.getGroup());

        verify(groupRepository).save(group);
    }

    @Test
    void updateGroup_groupNotFound_throws() {
        when(groupRepository.findById(42L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            groupService.updateGroup(42L, "Name", List.of(), List.of());
        });

        assertEquals("Group not found", ex.getMessage());
    }

    @Test
    void addStudentsToGroup_success() {
        Long groupId = 1L;
        Group group = new Group();
        group.setStudents(new ArrayList<>());

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        Student student = new Student();
        student.setUsername("student1");
        student.setRole(Roles.STUDENT);

        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(student));

        groupService.addStudentsToGroup(groupId, List.of("student1"));

        assertTrue(group.getStudents().contains(student));
        assertEquals(group, student.getGroup());

        verify(groupRepository).save(group);
    }

    @Test
    void addStudentsToGroup_groupNotFound_throws() {
        when(groupRepository.findById(5L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            groupService.addStudentsToGroup(5L, List.of("student1"));
        });

        assertEquals("Group not found", ex.getMessage());
    }

    @Test
    void addStudentsToGroup_userNotStudent_throws() {
        Long groupId = 1L;
        Group group = new Group();
        group.setStudents(new ArrayList<>());

        User user = new User();
        user.setUsername("notStudent");
        user.setRole(Roles.TEACHER);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findByUsername("notStudent")).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            groupService.addStudentsToGroup(groupId, List.of("notStudent"));
        });

        assertEquals("notStudent is not a student", ex.getMessage());
    }

    @Test
    void deleteGroup_success() {
        Long groupId = 1L;
        Group group = new Group();

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        groupService.deleteGroup(groupId);

        verify(groupRepository).delete(group);
    }

    @Test
    void deleteGroup_notFound_throws() {
        when(groupRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            groupService.deleteGroup(2L);
        });

        assertEquals("Group with id 2 not found", ex.getMessage());
    }

    @Test
    void getGroup_found() {
        Group group = new Group();
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        Optional<Group> result = groupService.getGroup(1L);
        assertTrue(result.isPresent());
        assertEquals(group, result.get());
    }

    @Test
    void getGroup_notFound() {
        when(groupRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Group> result = groupService.getGroup(2L);
        assertTrue(result.isEmpty());
    }
}
