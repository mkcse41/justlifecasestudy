package com.justlife.casestudy.dto.response;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Mukesh.K
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotResDTO {

	private LocalTime start;
	private LocalTime end;

}
