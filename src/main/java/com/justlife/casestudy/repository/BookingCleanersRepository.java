package com.justlife.casestudy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.justlife.casestudy.model.BookingCleaners;

/**
 * 
 * @author Mukesh.K
 *
 */
@Repository
public interface BookingCleanersRepository extends JpaRepository<BookingCleaners, Long> {

	List<BookingCleaners> findByProfessionalCleanerId(Long professionalCleanerId);

	List<BookingCleaners> findByBookingId(Long bookingId);

	List<BookingCleaners> findByBookingIdIn(List<Long> bookingIds);

	@Modifying
	@Query(value = "UPDATE BOOKING_CLEANERS SET ASSIGNMENT_STATUS = 'RELEASED' WHERE BOOKING_ID = :bookingId", nativeQuery = true)
	void releaseCleaners(@Param("bookingId") Long bookingId);
}