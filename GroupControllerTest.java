package com.Gestion_Note.Note.Controllers;

import com.Gestion_Note.Note.DTO.AddStudentsRequest;
import com.Gestion_Note.Note.DTO.GroupCreationRequest;
import com.Gestion_Note.Note.DTO.UpdateGroupRequest;
import com.Gestion_Note.Note.Entities.Group;
import com.Gestion_Note.Note.Services.GroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupController.class)
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSaveGroup_success() throws Exception {
        GroupCreationRequest request = new GroupCreationRequest("Group A", Collections.emptyList(), Collections.emptyList());

        mockMvc.perform(post("/group/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Group Saved"));

        verify(groupService).createGroup(request);
    }

    @Test
    void testGetAllGroups_success() throws Exception {
        Group group = new Group();
        group.setGroupName("G1");

        when(groupService.getAllGroups()).thenReturn(Collections.singletonList(group));

        mockMvc.perform(get("/group/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].groupName").value("G1"));
    }

    @Test
    void testGetGroup_success() throws Exception {
        Group group = new Group();
        group.setGroupName("G2");

        when(groupService.getGroup(1L)).thenReturn(Optional.of(group));

        mockMvc.perform(get("/group/get").param("groupId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupName").value("G2"));
    }

    @Test
    void testDeleteGroup_success() throws Exception {
        mockMvc.perform(delete("/group/delete").param("groupId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Group deleted successfully"));

        verify(groupService).deleteGroup(1L);
    }

    @Test
    void testUpdateGroup_success() throws Exception {
        UpdateGroupRequest updateRequest = new UpdateGroupRequest(1L, "Updated Group", Collections.emptyList(), Collections.emptyList());

        mockMvc.perform(put("/group/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Group updated successfully"));

        verify(groupService).updateGroup(1L, "Updated Group", Collections.emptyList(), Collections.emptyList());
    }

    @Test
    void testAddStudentsToGroup_success() throws Exception {
        AddStudentsRequest addRequest = new AddStudentsRequest(1L, Collections.singletonList("student1"));

        mockMvc.perform(put("/group/add-students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Students added to the group."));

        verify(groupService).addStudentsToGroup(1L, addRequest.getStudentUsernames());
    }
}
