package com.example.backend.Repository;

import com.example.backend.Entity.Scooter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScooterRepository extends CrudRepository<Scooter, Long> {

    Optional<Scooter> findById(int id);

    List<Scooter> findAll();
}
