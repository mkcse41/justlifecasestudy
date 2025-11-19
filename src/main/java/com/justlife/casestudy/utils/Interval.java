package com.justlife.casestudy.utils;

import java.time.LocalDateTime;

public class Interval {

	public LocalDateTime start;
	public LocalDateTime end;

	public Interval(LocalDateTime s, LocalDateTime e) {
		this.start = s;
		this.end = e;
	}
}