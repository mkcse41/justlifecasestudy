package com.justlife.casestudy.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Mukesh.K
 *
 */
@Entity
@Getter
@Setter
@Table(name = "BOOKING_CLEANERS")
public class BookingCleaners {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "BOOKING_ID")
	private Long bookingId;

	@Column(name = "PROFESSIONAL_CLEANER_ID")
	private Long professionalCleanerId;

	@Column(name = "ASSIGNMENT_STATUS", length = 10, nullable = false, unique = false)
	private String status;

	@Column(name = "CREATED_DT", nullable = true, unique = false)
	private Timestamp createdDt;

}
