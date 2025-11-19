package com.justlife.casestudy.rules;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.justlife.casestudy.constants.Constants;
import com.justlife.casestudy.exceptions.BadRequestException;
import com.justlife.casestudy.service.RuleConfigService;

/**
 * Validate booking within allowed working hours.
 * 
 * @author Mukesh.K
 *
 */
@Component
public class WorkingHoursRule implements BookingRule {

	private static final Logger logger = LoggerFactory.getLogger(WorkingHoursRule.class);

	@Autowired
	private RuleConfigService ruleConfigService;

	@Override
	public void validate(LocalDate date, LocalTime startTime, int durationHours) {

		logger.info("Validating WorkingHoursRule for date={}, startTime={}, durationHours={}", date, startTime,
				durationHours);

		LocalTime workStart = ruleConfigService.getTime(Constants.WORK_START_TIME);
		LocalTime workEnd = ruleConfigService.getTime(Constants.WORK_END_TIME);

		logger.info("Working hours from {} to {}", workStart, workEnd);

		// Build LocalDateTime for comparison
		LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
		LocalDateTime endDateTime = startDateTime.plusHours(durationHours);

		LocalDateTime workStartDateTime = LocalDateTime.of(date, workStart);
		LocalDateTime workEndDateTime = LocalDateTime.of(date, workEnd);

		logger.info("Calculated endDateTime={}", endDateTime);

		// If endDateTime goes to next day -> reject
		if (!endDateTime.toLocalDate().isEqual(date)) {
			logger.warn("Booking failed: endDateTime {} spills into next day", endDateTime);
			throw new BadRequestException("Booking cannot extend past " + workEnd);
		}

		// start before work start
		if (startDateTime.isBefore(workStartDateTime)) {
			logger.warn("Booking failed: startTime {} is before allowed {}", startTime, workStart);
			throw new BadRequestException("Booking cannot start before " + workStart);
		}

		// start after work end
		if (startDateTime.isAfter(workEndDateTime)) {
			logger.warn("Booking failed: startTime {} is after allowed {}", startTime, workEnd);
			throw new BadRequestException("Booking cannot start after " + workEnd);
		}

		// end after work end
		if (endDateTime.isAfter(workEndDateTime)) {
			logger.warn("Booking failed: endTime {} exceeds workEnd {}", endDateTime.toLocalTime(), workEnd);
			throw new BadRequestException("Booking must finish before " + workEnd);
		}

		logger.info("WorkingHoursRule validation passed for date {}", date);
	}

}
