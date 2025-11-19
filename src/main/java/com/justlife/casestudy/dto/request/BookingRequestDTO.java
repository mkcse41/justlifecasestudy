package com.justlife.casestudy.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Mukesh.K
 *
 */
@Setter
@Getter
public class BookingRequestDTO {

	private Long customerId;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Schema(type = "string", example = "2025-11-19", pattern = "yyyy-MM-dd")
	private LocalDate date;
	@JsonFormat(pattern = "HH:mm")
	@Schema(type = "string", example = "08:00", pattern = "HH:mm")
	private LocalTime startTime;
	@Schema(example = "2")
	private int durationHours;
	@Schema(example = "1")
	private int professionalCount;

}
