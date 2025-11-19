package com.justlife.casestudy.model;

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
@Table(name = "SYSTEM_RULES")
@Getter
@Setter
public class SystemRule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "RULE_KEY", nullable = false, unique = true, length = 50)
	private String ruleKey;

	@Column(name = "RULE_VALUE", nullable = false, length = 50)
	private String ruleValue;
}
