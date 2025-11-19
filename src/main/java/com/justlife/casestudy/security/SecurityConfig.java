package com.justlife.casestudy.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration
 *
 * @author Mukesh
 */
@Configuration
public class SecurityConfig {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	private final UserDetailsService userDetailsService;

	public SecurityConfig(@Qualifier("dbUserDetailsService") UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder encoder, UserDetailsService uds)
			throws Exception {

		logger.info("Initializing AuthenticationManager");
		AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authBuilder.userDetailsService(uds).passwordEncoder(encoder);
		return authBuilder.build();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService uds) {
		logger.info("Registering JwtAuthenticationFilter");
		return new JwtAuthenticationFilter(uds);
	}

	@Bean
	public org.springframework.security.web.SecurityFilterChain filterChain(HttpSecurity http,
			JwtAuthenticationFilter jwtFilter) throws Exception {

		logger.info("Setting up Security Filter Chain");

		http.csrf().disable().authorizeRequests()
				.antMatchers("/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
				.anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
