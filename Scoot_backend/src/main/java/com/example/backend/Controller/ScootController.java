package com.example.backend.Controller;


import com.example.backend.Entity.Scooter;
import com.example.backend.Repository.ScooterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping("/scooter")
@CrossOrigin
public class ScootController {

    ScooterRepository scooterRepository;

    public ScootController(ScooterRepository scooterRepository){
        this.scooterRepository = scooterRepository;
    }

    @PostMapping("/addScooter")
    public ResponseEntity<String> addScooter(@RequestBody Scooter scooter){
        scooterRepository.save(scooter);

        return ResponseEntity.status(HttpStatus.CREATED).body("Scooter created");

    }

    @GetMapping("/getScooter/{id}")
    public Optional<Scooter> getScooter(@PathVariable int id){
        return scooterRepository.findById(id);
    }
}
