package com.justlife.casestudy.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * 
 * @author Mukesh.K
 *
 */
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI justlifeOpenAPI() {

		return new OpenAPI()
				.info(new Info().title("Justlife Case Study API").version("1.0.0")
						.description("API documentation for booking, availability and scheduling"))
				.components(new Components().addSecuritySchemes("BearerAuth",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
				.security(List.of(new SecurityRequirement().addList("BearerAuth")));
	}
}
