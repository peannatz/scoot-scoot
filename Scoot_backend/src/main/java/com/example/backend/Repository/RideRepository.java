package com.example.backend.Repository;

import com.example.backend.Entity.Ride;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RideRepository extends CrudRepository<Ride, Integer> {

    Optional<Ride> findById(int id);
}
