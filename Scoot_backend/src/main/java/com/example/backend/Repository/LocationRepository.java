package com.example.backend.Repository;

import com.example.backend.Entity.Location;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LocationRepository extends CrudRepository<Location, Integer> {

    Optional<Location> findById(int id);
}
