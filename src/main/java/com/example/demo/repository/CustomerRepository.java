package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Find customer by NIC (National ID Card)
     * @param nic the customer's NIC number
     * @return Optional containing customer if found
     */
    Optional<Customer> findByNic(String nic);
    
    /**
     * Find customer by ID with all related data eagerly loaded
     * Prevents N+1 query problem
     * @param id the customer ID
     * @return Optional containing customer with all relationships loaded
     */
    @Query("SELECT DISTINCT c FROM Customer c " +
           "LEFT JOIN FETCH c.addresses a " +
           "LEFT JOIN FETCH a.city " +
           "LEFT JOIN FETCH a.country " +
           "LEFT JOIN FETCH c.familyMembers " +
           "WHERE c.id = :id")
    Optional<Customer> findByIdWithDetails(@Param("id") Long id);
    
    /**
     * Find customer with basic info and addresses only
     * Use when you don't need family members
     * @param id the customer ID
     * @return Optional containing customer with addresses
     */
    @Query("SELECT DISTINCT c FROM Customer c " +
           "LEFT JOIN FETCH c.addresses a " +
           "LEFT JOIN FETCH a.city " +
           "LEFT JOIN FETCH a.country " +
           "WHERE c.id = :id")
    Optional<Customer> findByIdWithAddresses(@Param("id") Long id);
    
    /**
     * Find customer with family members only
     * Use when you need family relationships
     * @param id the customer ID
     * @return Optional containing customer with family members
     */
    @Query("SELECT DISTINCT c FROM Customer c " +
           "LEFT JOIN FETCH c.familyMembers " +
           "WHERE c.id = :id")
    Optional<Customer> findByIdWithFamilyMembers(@Param("id") Long id);
    
    /**
     * Find all customers with their addresses
     * Useful for listing
     * @return list of customers with addresses
     */
    @Query("SELECT DISTINCT c FROM Customer c " +
           "LEFT JOIN FETCH c.addresses a " +
           "LEFT JOIN FETCH a.city " +
           "LEFT JOIN FETCH a.country")
    List<Customer> findAllWithAddresses();
    
    /**
     * Find customers by name (case-insensitive search)
     * @param name the customer name (partial or full)
     * @return list of matching customers
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Customer> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Find customers born between two dates
     * Useful for age-based queries
     * @param startDate the start date
     * @param endDate the end date
     * @return list of customers
     */
    @Query("SELECT c FROM Customer c WHERE c.dateOfBirth BETWEEN :startDate AND :endDate")
    List<Customer> findByDateOfBirthBetween(@Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
    
    /**
     * Find all customers living in a specific city
     * @param cityId the city ID
     * @return list of customers
     */
    @Query("SELECT DISTINCT c FROM Customer c " +
           "JOIN c.addresses a " +
           "WHERE a.city.id = :cityId")
    List<Customer> findByCity(@Param("cityId") Long cityId);
    
    /**
     * Find all customers in a specific country
     * @param countryId the country ID
     * @return list of customers
     */
    @Query("SELECT DISTINCT c FROM Customer c " +
           "JOIN c.addresses a " +
           "WHERE a.country.id = :countryId")
    List<Customer> findByCountry(@Param("countryId") Long countryId);
    
    /**
     * Check if customer with NIC already exists
     * @param nic the NIC number
     * @return true if exists, false otherwise
     */
    boolean existsByNic(String nic);
    
    /**
     * Count total customers
     * @return total count
     */
    long count();
}