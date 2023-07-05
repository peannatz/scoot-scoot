package com.example.backend.Enum;

public enum TierType {
    BASIC(8, 20),
    PREMIUM(40, 100);

    final int minutePrice;
    final int kilometrePrice;

    TierType(int minutePrice, int kilometrePrice) {
        this.minutePrice = minutePrice;
        this.kilometrePrice = kilometrePrice;
    }

    public int getMinutePrice() {
        return minutePrice;
    }

    public int getKilometrePrice() {
        return kilometrePrice;
    }
}
