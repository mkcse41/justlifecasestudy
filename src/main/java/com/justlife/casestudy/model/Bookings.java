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
@Setter
@Getter
@Table(name = "BOOKINGS")
public class Bookings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "BOOKING_ID", length = 10, nullable = false, unique = true)
	private String bookingId;

	@Column(name = "CUSTOMER_ID")
	private Long customerId;

	@Column(name = "STARTED_AT", nullable = true, unique = false)
	private Timestamp startedAt;

	@Column(name = "DURATION_HOURS")
	private int durationHours;

	@Column(name = "PROFESSION_CLEANER_COUNT")
	private int professionalCleanerCount;

	@Column(name = "STATUS", length = 10, nullable = false, unique = false)
	private String status;

	@Column(name = "CREATED_DT", nullable = true, unique = false)
	private Timestamp createdDt;

	@Column(name = "UPDATED_DT", nullable = true, unique = false)
	private Timestamp updatedDt;

}
