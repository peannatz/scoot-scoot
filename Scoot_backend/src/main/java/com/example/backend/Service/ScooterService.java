package com.example.backend.Service;

import com.example.backend.DTO.ScooterDto;
import com.example.backend.DTO.TierTypeDto;
import com.example.backend.Entity.Scooter;
import com.example.backend.Repository.ScooterRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScooterService {

    ScooterRepository scooterRepository;

    public ScooterService(ScooterRepository scooterRepository){
        this.scooterRepository = scooterRepository;
    }

    public List<Scooter> getScootersByBattery(int battery){
        List<Scooter> scooterList = scooterRepository.findAll();
        List<Scooter> scooterBatteryList = new ArrayList<>();
        for (Scooter scooter:scooterList
        ) {
            if(scooter.getBattery() > battery){
                scooterBatteryList.add(scooter);
            }
        }
        return scooterBatteryList;
    }

    public ScooterDto getWithTierType(long id){
        Scooter scooter = scooterRepository.findById(id).orElseThrow();
        TierTypeDto tierTypeDto = new TierTypeDto(scooter.getTier(), scooter.getTier().getMinutePrice(), scooter.getTier().getKilometrePrice());
        return new ScooterDto(scooter.getId(),scooter.getName(), scooter.getBattery(), scooter.isAvailable(), scooter.getLocation(), tierTypeDto);
    }
}
