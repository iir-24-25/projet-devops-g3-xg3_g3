package com.Gestion_Note.Note.Controllers;


import com.Gestion_Note.Note.DTO.AddStudentsRequest;
import com.Gestion_Note.Note.DTO.GroupCreationRequest;
import com.Gestion_Note.Note.DTO.UpdateGroupRequest;
import com.Gestion_Note.Note.Entities.Group;
import com.Gestion_Note.Note.Services.GroupService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveGroup(@RequestBody GroupCreationRequest group){
        try {
            groupService.createGroup(group);
            return ResponseEntity.ok("Group Saved ");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error " + e.getMessage());
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllGroups(){
        try{
            List<Group> groups = groupService.getAllGroups();
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error " + e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getGroup(@RequestParam Long groupId){
        try {
            Optional<Group> group = groupService.getGroup(groupId);
            return ResponseEntity.ok(group);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGroup(@RequestParam Long groupId) {
        try {
            groupService.deleteGroup(groupId);
            return ResponseEntity.ok("Group deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateGroup(@RequestBody UpdateGroupRequest request) {
        try {
            groupService.updateGroup(request.getGroupId(), request.getGroupName(), request.getTeacherUsernames(), request.getStudentUsernames());
            return ResponseEntity.ok("Group updated successfully");
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/add-students")
    public ResponseEntity<?> addStudentsToGroup(@RequestBody AddStudentsRequest request) {
        try {
            groupService.addStudentsToGroup(request.getGroupId(), request.getStudentUsernames());
            return ResponseEntity.ok("Students added to the group.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
