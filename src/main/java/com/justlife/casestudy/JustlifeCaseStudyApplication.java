package com.justlife.casestudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 
 * @author Mukesh.Kumar
 *
 */
@SpringBootApplication
@EnableScheduling
public class JustlifeCaseStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(JustlifeCaseStudyApplication.class, args);
	}

}
