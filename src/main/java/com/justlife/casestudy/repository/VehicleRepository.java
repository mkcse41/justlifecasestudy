package com.justlife.casestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.justlife.casestudy.model.Vehicle;

/**
 * 
 * @author Mukesh.K
 *
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

}
