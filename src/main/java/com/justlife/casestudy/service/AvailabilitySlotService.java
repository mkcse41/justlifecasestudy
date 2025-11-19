package com.justlife.casestudy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.justlife.casestudy.dto.response.TimeSlotResDTO;
import com.justlife.casestudy.utils.Interval;

/**
 * 
 * @author Mukesh.Kumar
 *
 */
@Component
public class AvailabilitySlotService {

	public List<TimeSlotResDTO> calculateFreeSlots(LocalDate date, LocalTime workStart, LocalTime workEnd,
			List<Interval> busyIntervals) {

		List<TimeSlotResDTO> freeSlots = new ArrayList<>();

		LocalDateTime cursor = LocalDateTime.of(date, workStart);
		LocalDateTime dayEnd = LocalDateTime.of(date, workEnd);

		for (Interval interval : busyIntervals) {

			if (cursor.isBefore(interval.start)) {
				freeSlots.add(createSlot(cursor, interval.start));
			}

			if (cursor.isBefore(interval.end)) {
				cursor = interval.end;
			}
		}

		if (cursor.isBefore(dayEnd)) {
			freeSlots.add(createSlot(cursor, dayEnd));
		}

		return freeSlots;
	}

	private TimeSlotResDTO createSlot(LocalDateTime start, LocalDateTime end) {
		TimeSlotResDTO dto = new TimeSlotResDTO();
		dto.setStart(start.toLocalTime());
		dto.setEnd(end.toLocalTime());
		return dto;
	}

}
