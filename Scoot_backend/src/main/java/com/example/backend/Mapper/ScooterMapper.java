package com.example.backend.Mapper;

import com.example.backend.DTO.ScooterDto;
import com.example.backend.DTO.TierTypeDto;
import com.example.backend.Entity.Scooter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ScooterMapper {

    ModelMapper modelMapper;

    public ScooterMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ScooterDto getScooterDto(Scooter scooter, TierTypeDto tierTypeDto){
        return new ScooterDto(scooter.getId(),scooter.getName(), scooter.getBattery(), scooter.isAvailable(), scooter.getLocation(), tierTypeDto);
    }
}
