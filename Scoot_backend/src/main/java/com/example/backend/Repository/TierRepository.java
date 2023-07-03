package com.example.backend.Repository;

import com.example.backend.Entity.Tier;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TierRepository extends CrudRepository<Tier, Integer> {

    Optional<Tier> findById(int id);
}
