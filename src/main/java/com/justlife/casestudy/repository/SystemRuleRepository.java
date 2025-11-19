package com.justlife.casestudy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.justlife.casestudy.model.SystemRule;

/**
 * 
 * @author Mukesh.K
 *
 */
@Repository
public interface SystemRuleRepository extends JpaRepository<SystemRule, Long> {

	Optional<SystemRule> findByRuleKey(String ruleKey);
}