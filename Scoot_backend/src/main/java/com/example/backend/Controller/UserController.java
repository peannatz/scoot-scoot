package com.example.backend.Controller;


import com.example.backend.Entity.User;
import com.example.backend.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody User user){
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @GetMapping("/getbyId/{id}")
    public Optional<User> getById(@PathVariable int id){
        return userRepository.findById(id);
    }

    @GetMapping("/getByEmail/{email}")
    public Optional<User> getByEmail(@PathVariable String email){
        return userRepository.findByEmail(email);
    }
}
