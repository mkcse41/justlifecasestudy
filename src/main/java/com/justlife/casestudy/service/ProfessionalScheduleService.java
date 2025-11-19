package com.justlife.casestudy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.justlife.casestudy.model.BookingCleaners;
import com.justlife.casestudy.model.Bookings;
import com.justlife.casestudy.repository.BookingCleanersRepository;
import com.justlife.casestudy.repository.BookingRepository;
import com.justlife.casestudy.utils.Interval;

/**
 * 
 * @author Mukesh.K
 *
 */
@Component
public class ProfessionalScheduleService {

	private final BookingRepository bookingRepo;
	private final BookingCleanersRepository bookingCleanersRepo;

	public ProfessionalScheduleService(BookingRepository bookingRepo, BookingCleanersRepository bookingCleanersRepo) {
		this.bookingRepo = bookingRepo;
		this.bookingCleanersRepo = bookingCleanersRepo;
	}

	/**
	 * STEP 1: Get all busy intervals for a given professional & date
	 */
	public List<Interval> getBusySlotsForProfessional(Long professionalId, LocalDate date, int breakMinutes) {

		// Get bookings assigned to this professional
		List<BookingCleaners> assignments = bookingCleanersRepo.findByProfessionalCleanerId(professionalId);

		List<Interval> intervals = new ArrayList<>();

		for (BookingCleaners bc : assignments) {
			Bookings booking = bookingRepo.findById(bc.getBookingId()).orElse(null);
			if (booking == null)
				continue;

			LocalDateTime start = booking.getStartedAt().toLocalDateTime();
			LocalDateTime end = start.plusHours(booking.getDurationHours());

			if (!start.toLocalDate().equals(date))
				continue; // ignore other dates

			// apply break after booking
			end = end.plusMinutes(breakMinutes);

			intervals.add(new Interval(start, end));
		}

		// sort & merge
		Collections.sort(intervals, Comparator.comparing(i -> i.start));
		return merge(intervals);
	}

	/** Merge overlapping intervals */
	private List<Interval> merge(List<Interval> list) {
		if (list.isEmpty())
			return list;

		List<Interval> merged = new ArrayList<>();
		Interval current = list.get(0);

		for (int i = 1; i < list.size(); i++) {
			Interval next = list.get(i);

			if (!current.end.isBefore(next.start)) {
				current.end = current.end.isAfter(next.end) ? current.end : next.end;
			} else {
				merged.add(current);
				current = next;
			}
		}
		merged.add(current);
		return merged;
	}
}