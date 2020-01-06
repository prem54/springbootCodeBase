package com.capstone.project.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StopWatch;

import com.google.common.collect.Lists;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Profile("capstone-swagger")
@EnableSwagger2
public class SwaggerAutoConfiguration {
	
	private final Logger log = LoggerFactory.getLogger(SwaggerAutoConfiguration.class);
	
	static final String STARTING_MESSAGE = "Starting Swagger";
    static final String STARTED_MESSAGE = "Started Swagger in {} ms";
    public static final String DEFAULT_INCLUDE_PATTERN = "/.*";
    
    StopWatch watch = new StopWatch();
	
	@Bean
	public Docket swaggerConfiguration(){
		log.debug(STARTING_MESSAGE);
		watch.start();
		
		List<SecurityScheme> securitySchemes = new ArrayList<>();
		
		securitySchemes.add(new ApiKey("JWT", "Authorization", "header"));
		
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
//				.select()
//				.paths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
//				.apis(RequestHandlerSelectors.basePackage("com.capstone.project.controller"))
//				.build()
				.forCodeGeneration(true)
				.securityContexts(Lists.newArrayList(securityContext()))
				.securitySchemes(securitySchemes)
				.apiInfo(apiDetails());
		
		docket = docket.select()
	            .paths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
	            .apis(RequestHandlerSelectors.basePackage("com.capstone.project"))
	            .build();
		
        watch.stop();
        log.debug(STARTED_MESSAGE, watch.getTotalTimeMillis());
        
        return docket;
		
	}
	
	private ApiInfo apiDetails(){
		return new ApiInfo("Capstone API", "Capstone API documentation", "0.0.1", "", new springfox.documentation.service.Contact("", "", ""), "", "", Collections.emptyList());
	}
	
	private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
            .build();
    }
	
	List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
            new SecurityReference("JWT", authorizationScopes));
    }

}
