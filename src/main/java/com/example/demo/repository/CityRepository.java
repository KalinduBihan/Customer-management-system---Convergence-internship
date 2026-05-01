package com.example.demo.repository;

import com.example.demo.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    
    /**
     * Find city by name
     * @param name the city name
     * @return Optional containing city if found
     */
    Optional<City> findByName(String name);
}