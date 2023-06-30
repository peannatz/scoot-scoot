package com.example.backend.Service;

import com.example.backend.Entity.User;
import com.example.backend.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    ModelMapper modelMapper;
    UserRepository userRepository;

    public UserService(UserRepository userRepository, ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public void updateUser(int id, User user){
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Cant find User with the given Id"));
        modelMapper.map(user, existingUser);
        userRepository.save(existingUser);
    }
}
