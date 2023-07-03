package com.example.backend.Entity;

import com.example.backend.Enum.TierType;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tiers")
public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    int priceMin;
    int priceKm;

    @Enumerated(EnumType.STRING)
    TierType tier;

    @OneToMany(mappedBy = "tier")
    private List<Scooter> scooter;

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

    public TierType getTier() {
        return tier;
    }

    public void setTier(TierType tier) {
        this.tier = tier;
    }

    public List<Scooter> getScooter() {
        return scooter;
    }

    public void setScooter(List<Scooter> scooter) {
        this.scooter = scooter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
