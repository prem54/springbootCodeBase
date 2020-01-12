package com.capstone.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.capstone.project.security.JwtAuthenticationEntryPoint;
import com.capstone.project.security.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private UserDetailsService myUserDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
					// we don't need CSRF because our token is invulnerable
					.csrf()
					.disable()
					.authorizeRequests()
					.antMatchers(
							HttpMethod.GET,
		                    "/",
		                    "/v2/api-docs",           // swagger
		                    "/webjars/**",            // swagger-ui webjars
		                    "/swagger-resources/**",  // swagger-ui resources
		                    "/configuration/**",      // swagger configuration
		                    "/*.html",
		                    "/favicon.ico",
		                    "/**/*.html",
		                    "/**/*.css",
		                    "/**/*.js"
		           ).permitAll()
					.antMatchers("/private/**").permitAll()
					.antMatchers("/api/v1/addEmployee", "/api/v1/deleteEmployee/**").hasRole("ADMIN")
					.anyRequest()
					.authenticated()
				.and()
					.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		// Custom JWT based security filter
		httpSecurity
					.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
		// disable page caching
		httpSecurity
					.headers().cacheControl();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}
