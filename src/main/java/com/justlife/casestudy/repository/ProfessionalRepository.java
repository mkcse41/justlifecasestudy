package com.justlife.casestudy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.justlife.casestudy.model.Professionals;

/**
 * 
 * @author Mukesh.K
 *
 */
@Repository
public interface ProfessionalRepository extends JpaRepository<Professionals, Long> {

	@Query(value = "SELECT * FROM PROFESSIONAL WHERE IS_ACTIVE = 1 AND SERVICE_TYPE = :serviceType", nativeQuery = true)
	List<Professionals> getCarCleanerProfessionals(@Param("serviceType") String serviceType);

	@Query(value = "SELECT p.* FROM PROFESSIONAL p "
			+ "INNER JOIN BOOKING_CLEANERS bc ON p.ID = bc.PROFESSIONAL_CLEANER_ID "
			+ "WHERE bc.BOOKING_ID = :bookingId", nativeQuery = true)
	List<Professionals> findProfessionalsByBookingId(@Param("bookingId") Long bookingId);

}