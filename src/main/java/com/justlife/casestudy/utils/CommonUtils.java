package com.justlife.casestudy.utils;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Mukesh.K
 *
 */
@Component
public class CommonUtils {

	public Timestamp getSQLTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	public String generateNumber(int numberLength, boolean letter, boolean number) {
		return RandomStringUtils.random(numberLength, letter, number).toUpperCase();
	}

	public String generateBookingId(String startingChars) {
		return startingChars + generateNumber(6, false, true);
	}
}
