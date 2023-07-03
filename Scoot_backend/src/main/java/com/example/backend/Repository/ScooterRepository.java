package com.example.backend.Repository;

import com.example.backend.Entity.Scooter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScooterRepository extends CrudRepository<Scooter, Long> {

    List<Scooter> findAll();

    List<Scooter> findByAvailable(boolean available);
}
