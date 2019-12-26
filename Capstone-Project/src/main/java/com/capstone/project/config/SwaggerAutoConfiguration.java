package com.capstone.project.config;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Profile("capstone-swagger")
@EnableSwagger2
public class SwaggerAutoConfiguration {
	
	private final Logger log = LoggerFactory.getLogger(SwaggerAutoConfiguration.class);
	
	static final String STARTING_MESSAGE = "Starting Swagger";
    static final String STARTED_MESSAGE = "Started Swagger";
	
	@Bean
	public Docket swaggerConfiguration(){
		log.debug(STARTING_MESSAGE);
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.regex("/.*"))
				.apis(RequestHandlerSelectors.basePackage("com.capstone.project.controller"))
				.build()
				.apiInfo(apiDetails());
	}
	
	private ApiInfo apiDetails(){
		return new ApiInfo("Capstone API", "Capstone API documentation", "0.0.1", "", new springfox.documentation.service.Contact("", "", ""), "", "", Collections.emptyList());
	} 

}
