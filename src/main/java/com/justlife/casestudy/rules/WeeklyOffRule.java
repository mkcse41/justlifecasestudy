package com.justlife.casestudy.rules;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.justlife.casestudy.constants.Constants;
import com.justlife.casestudy.exceptions.BadRequestException;
import com.justlife.casestudy.service.RuleConfigService;

/**
 * Weekly off-day — no bookings allowed on configured weekday (e.g., FRIDAY)
 * 
 * @author Mukesh.K
 */
@Component
public class WeeklyOffRule implements BookingRule {

	private static final Logger logger = LoggerFactory.getLogger(WeeklyOffRule.class);

	@Autowired
	private RuleConfigService ruleConfigService;

	@Override
	public void validate(LocalDate date, LocalTime startTime, int durationHours) {

		logger.info("Validating WeeklyOffRule for date={}", date);

		String offDayStr = ruleConfigService.get(Constants.WEEKLY_OFF_DAY);

		if (offDayStr == null || offDayStr.isEmpty()) {
			logger.debug("No weekly off-day configured, skipping WeeklyOffRule.");
			return;
		}

		DayOfWeek offDay = DayOfWeek.valueOf(offDayStr.toUpperCase());
		DayOfWeek requestDay = date.getDayOfWeek();

		logger.info("Configured offDay = {}, booking request day = {}", offDay, requestDay);

		if (requestDay == offDay) {
			logger.warn("Booking rejected: {} is a weekly off-day ({})", date, offDay);
			throw new BadRequestException("No bookings allowed on " + offDay);
		}

		logger.info("WeeklyOffRule validation passed for date {}", date);
	}
}
