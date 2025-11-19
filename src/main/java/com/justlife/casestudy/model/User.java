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
@Table(name = "USERS")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "USERNAME", length = 50, nullable = false, unique = true)
	private String username;

	@Column(name = "PASSWORD", length = 128, nullable = true, unique = false)
	private String password;

	@Column(name = "FULL_NAME", length = 100, nullable = false, unique = false)
	private String fullName;

	@Column(name = "ROLE", length = 50, nullable = false, unique = false)
	private String role;

	@Column(name = "IS_ACTIVE", nullable = true, unique = false)
	private int isActive;

	@Column(name = "CREATED_DT", nullable = true, unique = false)
	private Timestamp createdDt;

	@Column(name = "UPDATED_DT", nullable = true, unique = false)
	private Timestamp updatedDt;

}
