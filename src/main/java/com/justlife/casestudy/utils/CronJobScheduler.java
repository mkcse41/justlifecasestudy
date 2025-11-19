package com.justlife.casestudy.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.justlife.casestudy.constants.Constants;
import com.justlife.casestudy.model.Bookings;
import com.justlife.casestudy.repository.BookingRepository;
import com.justlife.casestudy.service.IBookingService;

/**
 * 
 * @author Mukesh.K
 *
 */
@Component
public class CronJobScheduler {

	private static final Logger logger = LoggerFactory.getLogger(CronJobScheduler.class);

	@Autowired
	private BookingRepository bookingsRepository;

	@Autowired
	private IBookingService bookingService;

	@Scheduled(cron = "0 */5 * * * *") // every 5 minutes
	public void autoCompleteBookings() {

		logger.info("Running booking auto-complete scheduler...");

		LocalDateTime now = LocalDateTime.now();

		List<Bookings> dueBookings = bookingsRepository.findPendingBookingsToAutoComplete(Timestamp.valueOf(now));

		logger.info("Found {} bookings to auto-complete", dueBookings.size());

		for (Bookings booking : dueBookings) {

			logger.info("Completing booking id={} bookingId={}", booking.getId(), booking.getBookingId());

			booking.setStatus(Constants.COMPLETED);
			booking.setUpdatedDt(new Timestamp(System.currentTimeMillis()));
			bookingsRepository.save(booking);

			// release cleaner assignments
			bookingService.releaseCleanersForBooking(booking.getId());

			logger.info("Booking {} marked as COMPLETED and cleaners released", booking.getBookingId());
		}
	}

}
