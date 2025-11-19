package com.justlife.casestudy.dto.response;

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
public class CleanerAvailabilityResDTO {

	private Long id;
	private String professionalId;
	private String professionalName;
	private List<TimeSlotResDTO> availableSlots;

}
