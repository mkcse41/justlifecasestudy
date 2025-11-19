package com.justlife.casestudy.rules;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 
 * @author Mukesh.K
 *
 */
public interface BookingRule {

	void validate(LocalDate date, LocalTime startTime, int durationHours);
}