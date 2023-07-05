package com.example.backend.Controller;


import com.example.backend.DTO.ScooterDto;
import com.example.backend.DTO.TierTypeDto;
import com.example.backend.Entity.Scooter;
import com.example.backend.Enum.TierType;
import com.example.backend.Repository.ScooterRepository;
import com.example.backend.Service.ScooterService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public ScooterDto getById(@PathVariable long id){
        return scooterService.getWithTierType(id);
    }

    @GetMapping("/getAllScooters")
    public List<ScooterDto> getAllScooters(){
        List<Scooter> scooterList = scooterRepository.findAll();
        return scooterList.stream().map(scooter -> scooterService.getWithTierType(scooter.getId())).collect(Collectors.toList());
    }

    @GetMapping("/getAllAvailableScooters/{available}")
    public List<ScooterDto> getAllAvailableScooters(@PathVariable boolean available){
        List<Scooter> scooterList = scooterRepository.findByAvailable(available);
        return scooterList.stream().map(scooter -> scooterService.getWithTierType(scooter.getId())).collect(Collectors.toList());
    }

    @GetMapping("/getByBattery/{battery}")
    public List<ScooterDto> getScootersByBattery(@PathVariable int battery){
        List<Scooter> scooterList = scooterService.getScootersByBattery(battery);
        return scooterList.stream().map(scooter -> scooterService.getWithTierType(scooter.getId())).collect(Collectors.toList());
    }

    @GetMapping("/getPrices")
    public List<TierTypeDto> getPrices(){
        return Stream.of(TierType.values())
                .map(tierType -> new TierTypeDto(tierType, tierType.getMinutePrice(), tierType.getKilometrePrice()))
                .toList();
    }

    @PostMapping("/update/{id}")
    public void updateScooter(@PathVariable long id,@RequestBody Scooter scooter){
        scooterService.updateScooter(id, scooter);
    }

}
