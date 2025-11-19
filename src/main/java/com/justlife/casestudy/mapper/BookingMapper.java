package com.justlife.casestudy.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.justlife.casestudy.dto.response.BookingSummaryResDTO;
import com.justlife.casestudy.dto.response.CleanerAvailabilityResDTO;
import com.justlife.casestudy.model.Bookings;
import com.justlife.casestudy.model.Professionals;
import com.justlife.casestudy.model.User;

/**
 * 
 * @author Mukesh.K
 *
 */
@Component
public class BookingMapper {

	@Autowired
	private ModelMapper modelMapper;

	public CleanerAvailabilityResDTO convertProfessionalsEntityToRes(Professionals pro) {
		return modelMapper.map(pro, CleanerAvailabilityResDTO.class);
	}

	public BookingSummaryResDTO convertBookingEntityToRes(Bookings booking, List<Professionals> pros, User user) {
		BookingSummaryResDTO dto = modelMapper.map(booking, BookingSummaryResDTO.class);
		dto.setProfessionalNames(pros.stream().map(Professionals::getName).collect(Collectors.toList()));
		if (null != user)
			dto.setCustomerName(user.getFullName());
		if (booking.getStartedAt() != null) {
			LocalDateTime startDt = booking.getStartedAt().toLocalDateTime();
			dto.setDate(startDt.toLocalDate());
			dto.setStartTime(startDt.toLocalTime());
		}
		return dto;
	}

}
