package com.Gestion_Note.Note.Entities;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserConstructorAndGetters() {
        Long id = 1L;
        String name = "Safaa";
        String username = "safaa123";
        String email = "safaa@example.com";
        String password = "password123";
        Roles role = Roles.STUDENT;

        User user = new User(id, name, username, email, password, role);

        assertEquals(id, user.getUser_id());
        assertEquals(name, user.getName());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(role, user.getRole());
    }

    @Test
    public void testUserSetters() {
        User user = new User();

        user.setUser_id(2L);
        user.setName("Yasmine");
        user.setUsername("yasmine123");
        user.setEmail("yasmine@example.com");
        user.setPassword("securepass");
        user.setRole(Roles.TEACHER);

        assertEquals(2L, user.getUser_id());
        assertEquals("Yasmine", user.getName());
        assertEquals("yasmine123", user.getUsername());
        assertEquals("yasmine@example.com", user.getEmail());
        assertEquals("securepass", user.getPassword());
        assertEquals(Roles.TEACHER, user.getRole());
    }

    @Test
    public void testUserDetailsInterfaceMethods() {
        User user = new User(3L, "Admin", "admin", "admin@example.com", "adminpass", Roles.ADMIN);

        assertEquals("admin", user.getUsername());
        assertEquals("adminpass", user.getPassword());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }
}
