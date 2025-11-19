package com.justlife.casestudy.rules;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.justlife.casestudy.service.RuleConfigService;

/**
 * 
 * @author Mukesh.K
 *
 */
@Component
public class MinBreakRule implements BookingRule {

	@Autowired
	private RuleConfigService ruleConfigService;

	@Override
	public void validate(LocalDate date, LocalTime startTime, int durationHours) {
		// Actual min break check will be applied in BookingService against existing
		// bookings.
		// This rule just ensures config exists (can be extended).
		ruleConfigService.getInt("MIN_BREAK_MINUTES");
	}
}