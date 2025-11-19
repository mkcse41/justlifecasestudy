package com.justlife.carcleaning.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.justlife.casestudy.constants.Constants;
import com.justlife.casestudy.dto.request.BookingRequestDTO;
import com.justlife.casestudy.dto.request.BookingUpdateReqDTO;
import com.justlife.casestudy.dto.response.BookingSummaryResDTO;
import com.justlife.casestudy.dto.response.CleanerAvailabilityResDTO;
import com.justlife.casestudy.dto.response.TimeSlotResDTO;
import com.justlife.casestudy.exceptions.BadRequestException;
import com.justlife.casestudy.exceptions.DataNotFoundException;
import com.justlife.casestudy.mapper.BookingMapper;
import com.justlife.casestudy.model.Bookings;
import com.justlife.casestudy.model.Professionals;
import com.justlife.casestudy.model.User;
import com.justlife.casestudy.repository.BookingCleanersRepository;
import com.justlife.casestudy.repository.BookingRepository;
import com.justlife.casestudy.repository.ProfessionalRepository;
import com.justlife.casestudy.repository.UserRepository;
import com.justlife.casestudy.rules.BookingRuleEngine;
import com.justlife.casestudy.service.AvailabilitySlotService;
import com.justlife.casestudy.service.ProfessionalScheduleService;
import com.justlife.casestudy.service.RuleConfigService;
import com.justlife.casestudy.service.impl.BookingServiceImpl;
import com.justlife.casestudy.utils.CommonUtils;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

	@Spy
	@InjectMocks
	private BookingServiceImpl bookingService;

	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private BookingCleanersRepository bookingCleanersRepository;

	@Mock
	private ProfessionalRepository professionalRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BookingRuleEngine bookingRuleEngine;

	@Mock
	private RuleConfigService ruleConfigService;

	@Mock
	private CommonUtils commonUtils;

	@Mock
	private ProfessionalScheduleService scheduleService;

	@Mock
	private AvailabilitySlotService slotService;

	@Mock
	private BookingMapper bookingMapper;

	private User mockUser;
	private Professionals pro1;
	private Professionals pro2;

	@BeforeEach
	void setup() {

		mockUser = new User();
		mockUser.setId(1L);
		mockUser.setFullName("Mukesh Kumar");

		pro1 = new Professionals();
		pro1.setId(101L);
		pro1.setProfessionalId("P101");
		pro1.setName("Cleaner A");

		pro2 = new Professionals();
		pro2.setId(102L);
		pro2.setProfessionalId("P102");
		pro2.setName("Cleaner B");
	}

	@Test
	void testCreateBookingSuccess() {

		BookingRequestDTO req = new BookingRequestDTO();
		req.setCustomerId(1L);
		req.setDate(LocalDate.now());
		req.setStartTime(LocalTime.of(10, 0));
		req.setDurationHours(2);
		req.setProfessionalCount(1);

		when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

		doNothing().when(bookingRuleEngine).validate(any(), any(), anyInt());

		when(professionalRepository.getCarCleanerProfessionals(Constants.CAR_CLEANER)).thenReturn(List.of(pro1));

		when(scheduleService.getBusySlotsForProfessional(anyLong(), any(), anyInt())).thenReturn(List.of());

		when(slotService.calculateFreeSlots(any(), any(), any(), any()))
				.thenReturn(List.of(new TimeSlotResDTO(LocalTime.of(8, 0), LocalTime.of(22, 0))));

		Bookings saved = new Bookings();
		saved.setId(10L);
		saved.setBookingId("BK123");
		when(bookingRepository.save(any())).thenReturn(saved);

		when(professionalRepository.findAllById(anyList())).thenReturn(List.of(pro1));

		BookingSummaryResDTO mapped = new BookingSummaryResDTO();
		mapped.setBookingId("BK123");
		when(bookingMapper.convertBookingEntityToRes(any(), anyList(), any())).thenReturn(mapped);

		BookingSummaryResDTO result = bookingService.createBooking(req);

		assertNotNull(result);
		assertEquals("BK123", result.getBookingId());

		verify(bookingCleanersRepository, times(1)).save(any());
		verify(bookingRepository, times(1)).save(any());
		verify(bookingRuleEngine, times(2)).validate(any(), any(), anyInt());
	}

	@Test
	void testCreateBookingUserNotFound() {

		BookingRequestDTO req = new BookingRequestDTO();
		req.setCustomerId(99L);

		// Mock: user not found
		when(userRepository.findById(99L)).thenReturn(Optional.empty());

		// Expect exception
		assertThrows(DataNotFoundException.class, () -> bookingService.createBooking(req));

		// Ensure nothing is saved
		verify(bookingRepository, never()).save(any());
		verify(bookingCleanersRepository, never()).save(any());
	}

	@Test
	void testCreateBookingNotEnoughCleaners() {

		BookingRequestDTO req = new BookingRequestDTO();
		req.setCustomerId(1L);
		req.setProfessionalCount(2);
		req.setDate(LocalDate.now());
		req.setStartTime(LocalTime.of(10, 0));
		req.setDurationHours(2);

		when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

		doReturn(List.of()) // <-- FIXED
				.when(bookingService).getAvailabilityForSlot(any(), anyString(), anyInt());

		assertThrows(BadRequestException.class, () -> bookingService.createBooking(req));

		verify(bookingRepository, never()).save(any());
	}

	@Test
	void testUpdateBookingSuccess() {

		Bookings bk = new Bookings();
		bk.setId(5L);
		bk.setBookingId("BK001");
		bk.setCustomerId(1L);

		BookingUpdateReqDTO req = new BookingUpdateReqDTO();
		req.setBookingId("BK001");
		req.setDate(LocalDate.now());
		req.setStartTime(LocalTime.of(10, 0));
		req.setDurationHours(2);

		when(bookingRepository.findByBookingId("BK001")).thenReturn(bk);

		when(commonUtils.getSQLTimestamp()).thenReturn(new Timestamp(System.currentTimeMillis()));

		when(bookingRepository.saveAndFlush(any())).thenReturn(bk);

		when(professionalRepository.findProfessionalsByBookingId(5L)).thenReturn(List.of(pro1, pro2));

		when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

		BookingSummaryResDTO dto = new BookingSummaryResDTO();
		dto.setBookingId("BK001");

		when(bookingMapper.convertBookingEntityToRes(any(), anyList(), any())).thenReturn(dto);

		BookingSummaryResDTO response = bookingService.updateBooking(req);

		assertEquals("BK001", response.getBookingId());
	}

	@Test
	void testUpdateBooking_NotFound() {

		BookingUpdateReqDTO req = new BookingUpdateReqDTO();
		req.setBookingId("XXX");

		when(bookingRepository.findByBookingId("XXX")).thenReturn(null);

		assertThrows(DataNotFoundException.class, () -> bookingService.updateBooking(req));
	}

	@Test
	void testGetAvailabilityByDate() {

		when(ruleConfigService.getTime(Constants.WORK_START_TIME)).thenReturn(LocalTime.of(8, 0));

		when(ruleConfigService.getTime(Constants.WORK_END_TIME)).thenReturn(LocalTime.of(22, 0));

		when(ruleConfigService.getInt(Constants.MIN_BREAK_MINUTES)).thenReturn(30);

		when(professionalRepository.getCarCleanerProfessionals(Constants.CAR_CLEANER)).thenReturn(List.of(pro1));

		when(scheduleService.getBusySlotsForProfessional(anyLong(), any(), anyInt())).thenReturn(List.of());

		when(slotService.calculateFreeSlots(any(), any(), any(), any()))
				.thenReturn(List.of(new TimeSlotResDTO(LocalTime.of(8, 0), LocalTime.of(22, 0))));

		List<CleanerAvailabilityResDTO> response = bookingService.getAvailabilityByDate(LocalDate.now());

		assertEquals(1, response.size());
		assertEquals("Cleaner A", response.get(0).getProfessionalName());
	}

	@Test
	void testGetAvailabilityForSlot() {

		// Mock working hours & break
		when(ruleConfigService.getTime(Constants.WORK_START_TIME)).thenReturn(LocalTime.of(8, 0));
		when(ruleConfigService.getTime(Constants.WORK_END_TIME)).thenReturn(LocalTime.of(22, 0));
		when(ruleConfigService.getInt(Constants.MIN_BREAK_MINUTES)).thenReturn(30);

		// Mock professionals from DB
		Professionals pro = new Professionals();
		pro.setId(101L);
		pro.setName("Cleaner A");
		pro.setProfessionalId("P101");

		when(professionalRepository.getCarCleanerProfessionals(Constants.CAR_CLEANER)).thenReturn(List.of(pro));

		// Mock no busy slots
		when(scheduleService.getBusySlotsForProfessional(anyLong(), any(), anyInt())).thenReturn(List.of());

		// Mock free slots generated
		when(slotService.calculateFreeSlots(any(), any(), any(), any()))
				.thenReturn(List.of(new TimeSlotResDTO(LocalTime.of(8, 0), LocalTime.of(22, 0))));

		// No exception from rules
		doNothing().when(bookingRuleEngine).validate(any(), any(), anyInt());

		// Run method
		List<CleanerAvailabilityResDTO> result = bookingService.getAvailabilityForSlot(LocalDate.now(), "10:00", 2);

		// Verify
		assertEquals(1, result.size());
		assertEquals(101L, result.get(0).getId());
	}

}
