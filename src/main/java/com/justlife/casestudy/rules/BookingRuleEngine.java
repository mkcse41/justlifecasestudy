package com.justlife.casestudy.rules;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 
 * @author Mukesh.K
 *
 */
@Service
public class BookingRuleEngine {

	private final List<BookingRule> rules;

	public BookingRuleEngine(List<BookingRule> rules) {
		this.rules = rules;
	}

	public void validate(LocalDate date, LocalTime startTime, int durationHours) {
		for (BookingRule rule : rules) {
			rule.validate(date, startTime, durationHours);
		}
	}
}
