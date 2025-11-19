package com.justlife.casestudy.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Mukesh.K
 *
 */
@Setter
@Getter
public class BookingSummaryResDTO {

	private Long id;
	private String bookingId;
	private Long customerId;
	private String customerName;
	private LocalDate date;
	private LocalTime startTime;
	private int durationHours;
	private int professionalCleanerCount;
	private String status;
	private List<String> professionalNames;

}
