package com.example.demo.repository;

import com.example.demo.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    
    /**
     * Find country by name
     * @param name the country name
     * @return Optional containing country if found
     */
    Optional<Country> findByName(String name);
}