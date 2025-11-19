package com.justlife.casestudy.repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.justlife.casestudy.dto.response.IBookingResponseDTO;
import com.justlife.casestudy.model.Bookings;

/**
 * 
 * @author Mukesh.K
 *
 */
@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {

	Bookings findByBookingId(String bookingId);

	@Query(value = "SELECT * FROM BOOKINGS WHERE STATUS = 'CONFIRMED' "
			+ "AND DATE_ADD(STARTED_AT, INTERVAL DURATION_HOURS HOUR) <= :now", nativeQuery = true)
	List<Bookings> findPendingBookingsToAutoComplete(@Param("now") Timestamp now);

	@Query(value = "SELECT b.ID AS id, b.BOOKING_ID AS bookingId, b.CUSTOMER_ID AS customerId, "
			+ "u.FULL_NAME AS customerName, CONVERT_TZ(b.STARTED_AT, '+00:00', '+05:30') AS startedAt, "
			+ "b.DURATION_HOURS AS durationHours, "
			+ "b.PROFESSION_CLEANER_COUNT AS professionalCleanerCount, b.STATUS AS status, "
			+ "GROUP_CONCAT(p.NAME SEPARATOR ',') AS cleanerNames FROM BOOKINGS b "
			+ "LEFT JOIN USERS u ON u.ID = b.CUSTOMER_ID LEFT JOIN BOOKING_CLEANERS bc ON bc.BOOKING_ID = b.ID "
			+ "LEFT JOIN PROFESSIONAL p ON p.ID = bc.PROFESSIONAL_CLEANER_ID "
			+ "WHERE DATE(b.STARTED_AT) BETWEEN :startDate AND :endDate GROUP BY b.ID "
			+ "ORDER BY b.STARTED_AT ASC", nativeQuery = true)
	List<IBookingResponseDTO> getBookingsBetweenDates(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);
}
