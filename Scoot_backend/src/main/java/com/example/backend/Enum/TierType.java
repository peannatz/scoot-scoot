package com.example.backend.Enum;

public enum TierType {
    BASIC(2, 5),
    PREMIUM(10, 20);

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
