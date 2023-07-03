package com.example.backend.DTO;

import com.example.backend.Entity.Location;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ScooterDto(long id,
                         String name,
                         int battery,
                         boolean available,
                         Location location,
                         @JsonProperty("tierType") TierTypeDto tierTypeDto) {
}
