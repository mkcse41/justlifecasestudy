package com.justlife.casestudy.service;

import java.time.LocalDate;
import java.util.List;

import com.justlife.casestudy.dto.request.BookingRequestDTO;
import com.justlife.casestudy.dto.request.BookingUpdateReqDTO;
import com.justlife.casestudy.dto.response.BookingSummaryResDTO;
import com.justlife.casestudy.dto.response.CleanerAvailabilityResDTO;
import com.justlife.casestudy.dto.response.IBookingResponseDTO;

/**
 * 
 * @author Mukesh.K
 *
 */
public interface IBookingService {

	BookingSummaryResDTO createBooking(BookingRequestDTO request);

	List<CleanerAvailabilityResDTO> getAvailabilityByDate(LocalDate date);

	List<CleanerAvailabilityResDTO> getAvailabilityForSlot(LocalDate date, String startTime, int durationHours);

	BookingSummaryResDTO updateBooking(BookingUpdateReqDTO request);

	List<IBookingResponseDTO> getBookingsBetweenDates(LocalDate startDate, LocalDate endDate);

	void releaseCleanersForBooking(Long bookingId);

}
