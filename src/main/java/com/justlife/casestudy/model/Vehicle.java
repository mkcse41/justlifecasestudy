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
@Table(name = "VEHICLES")
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "CAR_NAME", length = 100, nullable = false, unique = false)
	private String carName;

	@Column(name = "CAR_REG_NUMBER", length = 10, nullable = false, unique = true)
	private String carRegNumber;

	@Column(name = "IS_ACTIVE", nullable = true, unique = false)
	private int isActive;

	@Column(name = "CREATED_DT", nullable = true, unique = false)
	private Timestamp createdDt;

	@Column(name = "UPDATED_DT", nullable = true, unique = false)
	private Timestamp updatedDt;

}
