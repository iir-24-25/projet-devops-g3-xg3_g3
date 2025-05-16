package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.Entities.Roles;
import com.Gestion_Note.Note.Entities.User;
import com.Gestion_Note.Note.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ReturnsUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void saveUser_Success() {
        User user = new User();
        user.setName("Alice");

        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.saveUser(user);

        assertEquals("Alice", saved.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUser_Found() {
        User user = new User();
        user.setUser_id(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUser(1L);

        assertEquals(1L, result.getUser_id());
    }

    @Test
    void getUser_NotFound_Throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUser(1L));
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void deleteUser_Success() {
        User user = new User();
        user.setUser_id(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_NotFound_Throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        assertTrue(ex.getMessage().contains("User Not Found"));
        verify(userRepository, never()).delete(any());
    }

    @Test
    void updateUser_WithPassword_EncodesPassword() {
        User existingUser = new User();
        existingUser.setUser_id(1L);
        existingUser.setPassword("oldPassword");

        User inputUser = new User();
        inputUser.setUser_id(1L);
        inputUser.setName("Bob");
        inputUser.setUsername("bob123");
        inputUser.setEmail("bob@example.com");
        inputUser.setRole(Roles.STUDENT);
        inputUser.setPassword("newPass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User updated = userService.updateUser(inputUser);

        assertEquals("Bob", updated.getName());
        assertEquals("bob123", updated.getUsername());
        assertEquals("bob@example.com", updated.getEmail());
        assertEquals(Roles.STUDENT, updated.getRole());
        assertEquals("encodedPass", updated.getPassword());

        verify(passwordEncoder, times(1)).encode("newPass");
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_WithoutPassword_DoesNotEncode() {
        User existingUser = new User();
        existingUser.setUser_id(1L);
        existingUser.setPassword("oldPassword");

        User inputUser = new User();
        inputUser.setUser_id(1L);
        inputUser.setName("Bob");
        inputUser.setUsername("bob123");
        inputUser.setEmail("bob@example.com");
        inputUser.setRole(Roles.TEACHER);
        inputUser.setPassword(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User updated = userService.updateUser(inputUser);

        assertEquals("Bob", updated.getName());
        assertEquals("bob123", updated.getUsername());
        assertEquals("bob@example.com", updated.getEmail());
        assertEquals(Roles.TEACHER, updated.getRole());
        assertEquals("oldPassword", updated.getPassword()); // unchanged

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_UserNotFound_Throws() {
        User inputUser = new User();
        inputUser.setUser_id(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.updateUser(inputUser));
        assertTrue(ex.getMessage().contains("User not Found"));
    }
}
