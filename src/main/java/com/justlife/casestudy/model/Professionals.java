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
@Table(name = "PROFESSIONAL")
public class Professionals {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "PROFESSIONAL_ID", length = 10, nullable = false, unique = true)
	private String professionalId;

	@Column(name = "NAME", length = 100, nullable = false, unique = false)
	private String name;

	@Column(name = "MOBILE_NUMBER", length = 10, nullable = false, unique = false)
	private String mobileNumber;

	@Column(name = "SERVICE_TYPE", length = 20, nullable = false, unique = false)
	private String serviceType;

	@Column(name = "IS_ACTIVE", nullable = true, unique = false)
	private int isActive;

	@Column(name = "CREATED_DT", nullable = true, unique = false)
	private Timestamp createdDt;

	@Column(name = "UPDATED_DT", nullable = true, unique = false)
	private Timestamp updatedDt;

}
