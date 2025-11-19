package com.justlife.casestudy.dto.response;

import java.sql.Timestamp;

/**
 * 
 * @author Mukesh.K
 *
 */
public interface IBookingResponseDTO {

	Long getId();

	String getBookingId();

	Long getCustomerId();

	String getCustomerName();

	Timestamp getStartedAt();

	Integer getDurationHours();

	Integer getProfessionalCleanerCount();

	String getStatus();

	String getCleanerNames();
}
