package com.Gestion_Note.Note.Controllers;

import com.Gestion_Note.Note.Entities.User;
import com.Gestion_Note.Note.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@EnableAutoMock
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUser_id(1L);
        testUser.setName("adnan");
        testUser.setEmail("adnan@example.com");
    }

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }

    // GET /user/all
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetAllUsers_success() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(testUser));

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user_id").value(testUser.getUser_id()))
                .andExpect(jsonPath("$[0].name").value(testUser.getName()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetAllUsers_noContent() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No users found"));
    }

    // PUT /user/update
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateUser_success() throws Exception {
        doNothing().when(userService).updateUser(testUser);

        mockMvc.perform(put("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Updated Successfully"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateUser_notFound() throws Exception {
        doThrow(new RuntimeException()).when(userService).updateUser(testUser);

        mockMvc.perform(put("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Not Found With ID : " + testUser.getUser_id()));
    }

    // GET /user/get
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetUser_success() throws Exception {
        when(userService.getUser(1L)).thenReturn(testUser);

        mockMvc.perform(get("/user/get")
                        .param("user_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1L))
                .andExpect(jsonPath("$.name").value("Safaa"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetUser_notFound() throws Exception {
        when(userService.getUser(1L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/user/get")
                        .param("user_id", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not Found with the ID : 1"));
    }

    // DELETE /user/delete
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteUser_success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/user/delete")
                        .param("user_id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted with the ID : 1"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteUser_notFound() throws Exception {
        doThrow(new RuntimeException()).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/user/delete")
                        .param("user_id", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not Found with the ID : 1"));
    }
}
