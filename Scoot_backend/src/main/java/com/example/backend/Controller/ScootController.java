package com.example.backend.Controller;


import com.example.backend.Entity.Scooter;
import com.example.backend.Repository.ScooterRepository;
import com.example.backend.Service.ScooterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/scooter")
@CrossOrigin
public class ScootController {

    ScooterRepository scooterRepository;
    ScooterService scooterService;

    public ScootController(ScooterRepository scooterRepository, ScooterService scooterService){
        this.scooterRepository = scooterRepository;
        this.scooterService = scooterService;
    }

    @PostMapping("/addScooter")
    public Scooter addScooter(@RequestBody Scooter scooter){
        scooterRepository.save(scooter);

        return scooter;

    }

    @GetMapping("/getbyId/{id}")
    public Optional<Scooter> getById(@PathVariable long id){
        return scooterRepository.findById(id);
    }

    @GetMapping("/getAllScooters")
    public List<Scooter> getAllScooters(){
        return scooterRepository.findAll();
    }

    @PostMapping("/getAllAvailableScooters/{available}")
    public List<Scooter> getAllAvailableScooters(@PathVariable boolean available){
        return scooterRepository.findByAvailable(available);
    }

    @GetMapping("/getByBattery/{battery}")
    public List<Scooter> getScootersByBattery(@PathVariable int battery){
        return scooterService.getScootersByBattery(battery);
    }
}
