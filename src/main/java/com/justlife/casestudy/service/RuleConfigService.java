package com.justlife.casestudy.service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.justlife.casestudy.repository.SystemRuleRepository;

/**
 * 
 * @author Mukesh.K
 *
 */
@Service
public class RuleConfigService {

	private static final Logger logger = LoggerFactory.getLogger(RuleConfigService.class);

	private final SystemRuleRepository ruleRepository;
	private final Map<String, String> cache = new HashMap<>();

	public RuleConfigService(SystemRuleRepository ruleRepository) {
		this.ruleRepository = ruleRepository;
	}

	@PostConstruct
	public void loadRules() {
		logger.info("Loading system rules into cache...");
		ruleRepository.findAll().forEach(r -> cache.put(r.getRuleKey(), r.getRuleValue()));
		logger.info("Loaded {} rules", cache.size());
	}

	public String get(String key) {
		return cache.get(key);
	}

	public LocalTime getTime(String key) {
		return LocalTime.parse(cache.get(key)); // expects HH:mm
	}

	public int getInt(String key) {
		return Integer.parseInt(cache.get(key));
	}
}