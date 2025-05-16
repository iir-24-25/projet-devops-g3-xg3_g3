package com.Gestion_Note.Note.Controllers;


import com.Gestion_Note.Note.Entities.User;
import com.Gestion_Note.Note.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No users found");
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Users Not Found");
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user){
        try {
            userService.updateUser(user);
            return ResponseEntity.ok("User Updated Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User Not Found With ID : " + user.getUser_id());
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @GetMapping("/get")
    public ResponseEntity<?> getUser (@RequestParam Long user_id){
        try {
            User user = userService.getUser(user_id);
            return ResponseEntity.ok( user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User not Found with the ID : " + user_id);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long user_id){
        try{
            userService.deleteUser(user_id);
            return ResponseEntity.ok("User deleted with the ID : " + user_id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User not Found with the ID : " + user_id);
        }
    }

}
