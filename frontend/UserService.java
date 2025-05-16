package com.Gestion_Note.Note.Services;

import com.Gestion_Note.Note.Entities.User;
import com.Gestion_Note.Note.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers(){
        List <User> users = userRepository.findAll();
        return users;
    }

    public User saveUser (User user){
        return userRepository.save(user);
    }

    public User getUser(Long user_id) {
        return userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + user_id));
    }

    public void deleteUser(Long user_id){
        User existingUser = userRepository.findById(user_id)
                .orElseThrow(()-> new RuntimeException("User Not Found With The ID : " + user_id));
        userRepository.delete(existingUser);
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not Found With The Id"));

        existingUser.setName(user.getName());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(existingUser);
    }


}
