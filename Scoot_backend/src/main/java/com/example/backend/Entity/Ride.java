package com.example.backend.Entity;

import jakarta.persistence.*;

import java.util.Date;
@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Temporal(TemporalType.TIMESTAMP)
    Date startTime;
    int rideLength;
    int price;
    @ManyToOne(cascade = CascadeType.ALL)
    User user;

    @ManyToOne
    Location startLocation;
    @ManyToOne
    Location endLocation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getRideLength() {
        return rideLength;
    }

    public void setRideLength(int rideLength) {
        this.rideLength = rideLength;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }
}
