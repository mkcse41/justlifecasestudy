package com.justlife.casestudy.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.justlife.casestudy.dto.request.BookingRequestDTO;
import com.justlife.casestudy.dto.request.BookingUpdateReqDTO;
import com.justlife.casestudy.dto.response.APIResponseDTO;
import com.justlife.casestudy.dto.response.BookingSummaryResDTO;
import com.justlife.casestudy.dto.response.CleanerAvailabilityResDTO;
import com.justlife.casestudy.dto.response.IBookingResponseDTO;
import com.justlife.casestudy.service.IBookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 
 * @author Mukesh.K
 *
 */
@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "Booking creation, update and cleaner availability APIs")
public class BookingController {

	private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

	@Autowired
	private IBookingService bookingService;

	@Operation(summary = "Create a new booking", description = "Creates a booking after validating rules, availability and assigning cleaners")
	@PostMapping("/create")
	public ResponseEntity<APIResponseDTO> createBooking(@RequestBody BookingRequestDTO request) {

		logger.info("API: Create Booking triggered for customerId={}, date={}, startTime={}, duration={}",
				request.getCustomerId(), request.getDate(), request.getStartTime(), request.getDurationHours());

		BookingSummaryResDTO result = bookingService.createBooking(request);

		logger.info("Booking created successfully. BookingId={}", result.getBookingId());

		return ResponseEntity.ok(APIResponseDTO.success(result));
	}

	@Operation(summary = "Get availability for all cleaners by date", description = "Returns available cleaners and their free time slots for the given date")
	@Parameters({ @Parameter(name = "date", description = "Date in yyyy-MM-dd", required = true) })
	@GetMapping("/availability")
	public ResponseEntity<APIResponseDTO> getAvailabilityByDate(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

		logger.info("API: Checking cleaner availability for date={}", date);

		List<CleanerAvailabilityResDTO> result = bookingService.getAvailabilityByDate(date);

		logger.info("Availability fetched for {} cleaners", result.size());

		return ResponseEntity.ok(APIResponseDTO.success(result));
	}

	@Operation(summary = "Check availability for specific time slot", description = "Returns list of cleaners available for exact time + duration")
	@Parameters({ @Parameter(name = "date", description = "Date in yyyy-MM-dd", required = true),
			@Parameter(name = "startTime", description = "Start time in HH:mm", required = true),
			@Parameter(name = "durationHours", description = "Duration in hours (2 or 4)", required = true) })
	@GetMapping("/availability/slot")
	public ResponseEntity<APIResponseDTO> getAvailabilityForSlot(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam("startTime") String startTime, @RequestParam("durationHours") int durationHours) {

		logger.info("API: Checking slot availability date={}, startTime={}, duration={}", date, startTime,
				durationHours);

		List<CleanerAvailabilityResDTO> result = bookingService.getAvailabilityForSlot(date, startTime, durationHours);

		logger.info("Slot availability found: {} cleaners", result.size());

		return ResponseEntity.ok(APIResponseDTO.success(result));
	}

	@Operation(summary = "Update booking", description = "Updates booking date, time and duration")
	@PostMapping("/update")
	public ResponseEntity<APIResponseDTO> updateBooking(@RequestBody BookingUpdateReqDTO request) {

		logger.info("API: Update Booking | bookingId={}, newDate={}, newStartTime={}, duration={}",
				request.getBookingId(), request.getDate(), request.getStartTime(), request.getDurationHours());

		BookingSummaryResDTO updatedBooking = bookingService.updateBooking(request);

		logger.info("Booking update completed | bookingId={}", request.getBookingId());

		return ResponseEntity.ok(APIResponseDTO.success(updatedBooking));
	}

	@Operation(summary = "Get bookings between two dates", description = "Fetch all bookings between two dates with assigned cleaners")
	@GetMapping("/by/dates")
	public ResponseEntity<APIResponseDTO> getBookingsBetweenDates(
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		logger.info("API: Fetch Booking | fromDate={}, toDate={}", startDate, endDate);

		List<IBookingResponseDTO> result = bookingService.getBookingsBetweenDates(startDate, endDate);

		return ResponseEntity.ok(APIResponseDTO.success(result));
	}

}
