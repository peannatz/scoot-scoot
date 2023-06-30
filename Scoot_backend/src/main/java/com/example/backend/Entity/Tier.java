package com.example.backend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tiers")
public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    int priceMin;
    int priceKm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public int getPriceKm() {
        return priceKm;
    }

    public void setPriceKm(int priceKm) {
        this.priceKm = priceKm;
    }
}
