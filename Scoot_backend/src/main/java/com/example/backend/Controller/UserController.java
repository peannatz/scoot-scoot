package com.example.backend.Controller;


import com.example.backend.Entity.User;
import com.example.backend.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController("/user")
@CrossOrigin
public class UserController {

    UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(User user){
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @GetMapping("/getUser/{id}")
    public Optional<User> getUser(@PathVariable int id){
        return userRepository.findById(id);
    }
}
