package com.justlife.casestudy.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.justlife.casestudy.constants.Constants;
import com.justlife.casestudy.constants.ErrorMsgConstants;
import com.justlife.casestudy.dto.request.BookingRequestDTO;
import com.justlife.casestudy.dto.request.BookingUpdateReqDTO;
import com.justlife.casestudy.dto.response.BookingSummaryResDTO;
import com.justlife.casestudy.dto.response.CleanerAvailabilityResDTO;
import com.justlife.casestudy.dto.response.IBookingResponseDTO;
import com.justlife.casestudy.dto.response.TimeSlotResDTO;
import com.justlife.casestudy.exceptions.BadRequestException;
import com.justlife.casestudy.exceptions.DataNotFoundException;
import com.justlife.casestudy.mapper.BookingMapper;
import com.justlife.casestudy.model.BookingCleaners;
import com.justlife.casestudy.model.Bookings;
import com.justlife.casestudy.model.Professionals;
import com.justlife.casestudy.model.User;
import com.justlife.casestudy.repository.BookingCleanersRepository;
import com.justlife.casestudy.repository.BookingRepository;
import com.justlife.casestudy.repository.ProfessionalRepository;
import com.justlife.casestudy.repository.UserRepository;
import com.justlife.casestudy.rules.BookingRuleEngine;
import com.justlife.casestudy.service.AvailabilitySlotService;
import com.justlife.casestudy.service.IBookingService;
import com.justlife.casestudy.service.ProfessionalScheduleService;
import com.justlife.casestudy.service.RuleConfigService;
import com.justlife.casestudy.utils.CommonUtils;
import com.justlife.casestudy.utils.Interval;

/**
 * 
 * @author Mukesh.K
 *
 */
@Service
public class BookingServiceImpl implements IBookingService {

	private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private BookingCleanersRepository bookingCleanersRepository;

	@Autowired
	private ProfessionalRepository professionalRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookingRuleEngine bookingRuleEngine;

	@Autowired
	private RuleConfigService ruleConfigService;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private ProfessionalScheduleService scheduleService;

	@Autowired
	private AvailabilitySlotService slotService;

	@Autowired
	private BookingMapper bookingMapper;

	@Transactional
	@Override
	public BookingSummaryResDTO createBooking(BookingRequestDTO request) {

		logger.info("Starting booking creation | customerId={}, date={}, startTime={}, duration={}, cleanersNeeded={}",
				request.getCustomerId(), request.getDate(), request.getStartTime(), request.getDurationHours(),
				request.getProfessionalCount());

		Optional<User> user = userRepository.findById(request.getCustomerId());
		if (user == null) {
			logger.warn("User not found | customerId={}", request.getCustomerId());
			throw new DataNotFoundException(ErrorMsgConstants.CUSTOMER_NOT_FOUND);
		}

		// STEP 1: Validate request
		logger.debug("Validating booking rules for requested slot...");
		bookingRuleEngine.validate(request.getDate(), request.getStartTime(), request.getDurationHours());

		// STEP 2: Fetch available cleaners
		logger.debug("Fetching availability for date={} startTime={} durationHours={}", request.getDate(),
				request.getStartTime(), request.getDurationHours());
		List<CleanerAvailabilityResDTO> availableCleaners = getAvailabilityForSlot(request.getDate(),
				request.getStartTime().toString(), request.getDurationHours());
		logger.info("Found {} available professionals for requested slot", availableCleaners.size());

		// STEP 3: Smart Cleaner Selection
		logger.debug("Selecting {} cleaners from {} available", request.getProfessionalCount(),
				availableCleaners.size());
		List<CleanerAvailabilityResDTO> selectedCleaners = selectCleaners(availableCleaners,
				request.getProfessionalCount());

		List<Long> selectedCleanerIds = selectedCleaners.stream().map(CleanerAvailabilityResDTO::getId)
				.collect(Collectors.toList());

		logger.info("Selected professional IDs for booking: {}", selectedCleanerIds);

		// Step 3.1 - Real Concurrency check
		Timestamp newStart = Timestamp.valueOf(request.getStartTime().atDate(request.getDate()));
		Timestamp newEnd = Timestamp
				.valueOf(request.getStartTime().plusHours(request.getDurationHours()).atDate(request.getDate()));

		checkCleanerConflicts(selectedCleanerIds, newStart, newEnd);

		// STEP 4: Save Booking
		Bookings booking = saveBooking(request);
		logger.info("Booking saved with internal ID={} and bookingId={}", booking.getId(), booking.getBookingId());

		// STEP 5: Assign cleaners
		assignCleanersToBooking(booking.getId(), selectedCleanerIds);
		logger.info("Assigned cleaners {} to booking {}", selectedCleanerIds, booking.getBookingId());

		// STEP 6: Prepare response DTO
		logger.debug("Preparing booking summary response for bookingId={}", booking.getBookingId());
		List<Professionals> pros = professionalRepository.findAllById(selectedCleanerIds);
		BookingSummaryResDTO response = bookingMapper.convertBookingEntityToRes(booking, pros, user.get());

		logger.info("Booking creation completed successfully | bookingId={}", booking.getBookingId());

		return response;
	}

	@Override
	public List<CleanerAvailabilityResDTO> getAvailabilityByDate(LocalDate date) {

		logger.info("Checking availability for date {}", date);

		LocalTime workStart = ruleConfigService.getTime(Constants.WORK_START_TIME);
		LocalTime workEnd = ruleConfigService.getTime(Constants.WORK_END_TIME);
		int breakMinutes = ruleConfigService.getInt(Constants.MIN_BREAK_MINUTES);

		List<Professionals> professionals = professionalRepository.getCarCleanerProfessionals(Constants.CAR_CLEANER);

		List<CleanerAvailabilityResDTO> response = new ArrayList<>();

		for (Professionals pro : professionals) {

			// STEP 1 — Busy slots
			List<Interval> busySlots = scheduleService.getBusySlotsForProfessional(pro.getId(), date, breakMinutes);

			// STEP 2 — Free slots
			List<TimeSlotResDTO> freeSlots = slotService.calculateFreeSlots(date, workStart, workEnd, busySlots);

			// STEP 3 — Map to DTO
			CleanerAvailabilityResDTO dto = new CleanerAvailabilityResDTO();
			dto.setId(pro.getId());
			dto.setProfessionalId(pro.getProfessionalId());
			dto.setProfessionalName(pro.getName());
			dto.setAvailableSlots(freeSlots);

			response.add(dto);
		}

		return response;
	}

	@Override
	public List<CleanerAvailabilityResDTO> getAvailabilityForSlot(LocalDate date, String startTimeStr,
			int durationHours) {
		LocalTime startTime = LocalTime.parse(startTimeStr);
		logger.info("Checking availability for date {} at {} duration {}", date, startTime, durationHours);

		// Validate with global rules first
		bookingRuleEngine.validate(date, startTime, durationHours);

		List<CleanerAvailabilityResDTO> allForDay = getAvailabilityByDate(date);

		LocalTime endTime = startTime.plusHours(durationHours);

		return allForDay.stream()
				.filter(ca -> ca.getAvailableSlots().stream()
						.anyMatch(slot -> !slot.getStart().isAfter(startTime) && !slot.getEnd().isBefore(endTime)))
				.collect(Collectors.toList());
	}

	private List<CleanerAvailabilityResDTO> selectCleaners(List<CleanerAvailabilityResDTO> available,
			int requiredCount) {
		if (available.size() < requiredCount) {
			throw new BadRequestException("Not enough cleaners available for requested slot.");
		}

		// Simple but correct: choose least busy cleaners first
		return available.stream().sorted(Comparator.comparingInt(a -> a.getAvailableSlots().size()))
				.limit(requiredCount).collect(Collectors.toList());
	}

	private Bookings saveBooking(BookingRequestDTO request) {

		Bookings booking = new Bookings();
		booking.setBookingId(commonUtils.generateBookingId(Constants.BOOKING_CAR_CLEANER_SR_CHARS));
		booking.setCustomerId(request.getCustomerId());

		LocalDateTime startDt = LocalDateTime.of(request.getDate(), request.getStartTime());
		booking.setStartedAt(Timestamp.valueOf(startDt));
		booking.setDurationHours(request.getDurationHours());
		booking.setProfessionalCleanerCount(request.getProfessionalCount());
		booking.setStatus(Constants.CONFIRMED);
		booking.setCreatedDt(Timestamp.from(Instant.now()));
		booking.setUpdatedDt(Timestamp.from(Instant.now()));

		return bookingRepository.save(booking);
	}

	private void assignCleanersToBooking(Long bookingId, List<Long> cleanerIds) {
		Timestamp now = Timestamp.from(Instant.now());
		for (Long cleanerId : cleanerIds) {
			BookingCleaners bc = new BookingCleaners();
			bc.setBookingId(bookingId);
			bc.setProfessionalCleanerId(cleanerId);
			bc.setStatus("ASSIGNED");
			bc.setCreatedDt(now);
			bookingCleanersRepository.save(bc);
		}
	}

	private void checkCleanerConflicts(List<Long> cleanerIds, Timestamp start, Timestamp end) {

		List<Long> conflicts = bookingCleanersRepository.findLockedConflictingAssignments(cleanerIds, start, end);

		if (!conflicts.isEmpty()) {
			logger.warn("Conflict detected! Cleaners already booked. Conflicting rows = {}", conflicts);
			throw new BadRequestException("Selected cleaners are already booked for this timeframe.");
		}
	}

	@Override
	public BookingSummaryResDTO updateBooking(BookingUpdateReqDTO request) {

		logger.info("Updating booking | bookingId={}, newDate={}, newStartTime={}, newDuration={}",
				request.getBookingId(), request.getDate(), request.getStartTime(), request.getDurationHours());

		logger.debug("Validating booking rules for requested slot...");
		bookingRuleEngine.validate(request.getDate(), request.getStartTime(), request.getDurationHours());

		// Fetch existing booking
		Bookings booking = bookingRepository.findByBookingId(request.getBookingId());
		if (booking == null) {
			logger.warn("Booking not found | bookingId={}", request.getBookingId());
			throw new DataNotFoundException(ErrorMsgConstants.NO_DATA_FOUND);
		}

		logger.debug("Existing booking found | internalId={}, oldStartTime={}, oldDuration={}", booking.getId(),
				booking.getStartedAt(), booking.getDurationHours());

		// Update booking details
		booking.setDurationHours(request.getDurationHours());

		LocalDateTime updatedStartDt = LocalDateTime.of(request.getDate(), request.getStartTime());
		booking.setStartedAt(Timestamp.valueOf(updatedStartDt));

		booking.setUpdatedDt(commonUtils.getSQLTimestamp());

		booking = bookingRepository.saveAndFlush(booking);

		logger.info("Booking updated successfully | bookingId={}, newStart={}, newDuration={}", booking.getBookingId(),
				booking.getStartedAt(), request.getDurationHours());

		// Fetch assigned professionals
		List<Professionals> pros = professionalRepository.findProfessionalsByBookingId(booking.getId());
		logger.info("Fetched {} assigned professionals for bookingId={}", pros.size(), booking.getBookingId());

		Optional<User> user = userRepository.findById(booking.getCustomerId());
		return bookingMapper.convertBookingEntityToRes(booking, pros, user.get());
	}

	@Override
	public List<IBookingResponseDTO> getBookingsBetweenDates(LocalDate startDate, LocalDate endDate) {

		logger.info("Fetching bookings BETWEEN {} AND {}", startDate, endDate);

		List<IBookingResponseDTO> bookings = bookingRepository.getBookingsBetweenDates(startDate, endDate);

		logger.info("Total bookings found between dates: {}", bookings.size());

		return bookings;
	}

	@Transactional
	public void releaseCleanersForBooking(Long bookingId) {
		bookingCleanersRepository.releaseCleaners(bookingId);
	}

}
