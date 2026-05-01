package com.example.demo.repository;

import com.example.demo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    /**
     * Find all addresses for a specific customer
     * @param customerId the customer ID
     * @return list of addresses
     */
    @Query("SELECT a FROM Address a WHERE a.customer.id = :customerId")
    List<Address> findByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Find all addresses in a specific city for a customer
     * @param customerId the customer ID
     * @param cityId the city ID
     * @return list of addresses
     */
    @Query("SELECT a FROM Address a WHERE a.customer.id = :customerId AND a.city.id = :cityId")
    List<Address> findByCustomerIdAndCityId(@Param("customerId") Long customerId, @Param("cityId") Long cityId);
}