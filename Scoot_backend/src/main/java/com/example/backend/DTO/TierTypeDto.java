package com.example.backend.DTO;

import com.example.backend.Enum.TierType;

public record TierTypeDto(TierType tierType, int minutePrice, int kilometrePrice) {
}
