package com.example.backend.Service;

import com.example.backend.Entity.User;
import com.example.backend.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    ModelMapper modelMapper;
    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder){
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void updateUser(int id, User user){
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Cant find User with the given Id"));
        modelMapper.map(user, existingUser, "id");
        existingUser.setId(id);
        userRepository.save(existingUser);
    }

    public User saveUser(User user){
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

}
