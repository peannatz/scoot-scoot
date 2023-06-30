package com.example.backend.Controller;


import com.example.backend.Entity.User;
import com.example.backend.Repository.UserRepository;
import com.example.backend.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    UserRepository userRepository;
    UserService userService;

    public UserController(UserRepository userRepository, UserService userService){
        this.userRepository = userRepository;
        this.userService = userService;
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

    @PostMapping("/update/{Id}")
    public void updateUser(@PathVariable int id, @RequestBody User user){
        userService.updateUser(id, user);
    }
}
