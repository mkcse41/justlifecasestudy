package com.justlife.casestudy.dto.response;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Mukesh.K
 *
 */
@Setter
@Getter
public class TimeSlotResDTO {

	private LocalTime start;
	private LocalTime end;

}
