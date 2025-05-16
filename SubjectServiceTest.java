package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.DTO.SubjectDTO;
import com.Gestion_Note.Note.Entities.Roles;
import com.Gestion_Note.Note.Entities.Subject;
import com.Gestion_Note.Note.Entities.User;
import com.Gestion_Note.Note.Repository.SubjectRepository;
import com.Gestion_Note.Note.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubjectService subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addSubject_Success() {
        SubjectDTO dto = new SubjectDTO();
        dto.setName("Mathematics");
        dto.setTeacher("teacher1");

        User teacher = new User();
        teacher.setUsername("teacher1");
        teacher.setRole(Roles.TEACHER);

        when(userRepository.findByUsername("teacher1")).thenReturn(Optional.of(teacher));
        when(subjectRepository.save(any(Subject.class))).thenAnswer(i -> i.getArguments()[0]);

        String result = subjectService.addSubject(dto);

        assertEquals("Subject saved successfully", result);
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void addSubject_TeacherNotFound() {
        SubjectDTO dto = new SubjectDTO();
        dto.setName("Mathematics");
        dto.setTeacher("unknown");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> subjectService.addSubject(dto));
        assertTrue(ex.getMessage().contains("Teacher not found"));
        verify(subjectRepository, never()).save(any());
    }

    @Test
    void addSubject_UserNotTeacherRole() {
        SubjectDTO dto = new SubjectDTO();
        dto.setName("Mathematics");
        dto.setTeacher("user1");

        User user = new User();
        user.setUsername("user1");
        user.setRole(Roles.STUDENT); // Not a teacher

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> subjectService.addSubject(dto));
        assertTrue(ex.getMessage().contains("does not have the teacher role"));
        verify(subjectRepository, never()).save(any());
    }

    @Test
    void getAllSubjects_ReturnsList() {
        List<Subject> subjects = Arrays.asList(new Subject(), new Subject());
        when(subjectRepository.findAll()).thenReturn(subjects);

        List<Subject> result = subjectService.getAllSubjects();

        assertEquals(2, result.size());
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    void getSubject_Found() {
        Subject subject = new Subject();
        subject.setSubject_Id(1L);

        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        Optional<Subject> result = subjectService.getSubject(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getSubject_Id());
    }

    @Test
    void getSubject_NotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Subject> result = subjectService.getSubject(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void updateSubject_Success() {
        Long subjectId = 1L;
        SubjectDTO dto = new SubjectDTO();
        dto.setName("Physics");
        dto.setTeacher("teacher2");

        User teacher = new User();
        teacher.setUsername("teacher2");
        teacher.setRole(Roles.TEACHER);

        Subject existingSubject = new Subject();
        existingSubject.setSubject_Id(subjectId);
        existingSubject.setName("Math");

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(existingSubject));
        when(userRepository.findByUsername("teacher2")).thenReturn(Optional.of(teacher));
        when(subjectRepository.save(any(Subject.class))).thenAnswer(i -> i.getArguments()[0]);

        Subject updated = subjectService.updateSubject(subjectId, dto);

        assertEquals("Physics", updated.getName());
        assertEquals("teacher2", updated.getTeacherUsername());
        verify(subjectRepository, times(1)).save(existingSubject);
    }

    @Test
    void updateSubject_SubjectNotFound() {
        Long subjectId = 1L;
        SubjectDTO dto = new SubjectDTO();
        dto.setName("Physics");
        dto.setTeacher("teacher2");

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> subjectService.updateSubject(subjectId, dto));
        assertTrue(ex.getMessage().contains("Subject not found"));
    }

    @Test
    void deleteSubject_SubjectExists() {
        Long subjectId = 1L;
        Subject subject = new Subject();
        subject.setSubject_Id(subjectId);

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        doNothing().when(subjectRepository).delete(subject);

        subjectService.deleteSubject(subjectId);

        verify(subjectRepository, times(1)).delete(subject);
    }

    @Test
    void deleteSubject_SubjectNotFound() {
        Long subjectId = 1L;
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        // Should not throw, delete won't be called
        subjectService.deleteSubject(subjectId);

        verify(subjectRepository, never()).delete(any());
    }
}
