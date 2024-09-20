package com.vasiu_catalina.beauty_salon.security.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vasiu_catalina.beauty_salon.security.SecurityConstants;
import com.vasiu_catalina.beauty_salon.security.filter.ClientAuthenticationFilter;
import com.vasiu_catalina.beauty_salon.security.filter.EmployeeAuthenticationFilter;
import com.vasiu_catalina.beauty_salon.security.filter.ExceptionHandlerFilter;
import com.vasiu_catalina.beauty_salon.security.filter.JWTAuthorizationFilter;
import com.vasiu_catalina.beauty_salon.security.manager.ClientAuthenticationManager;
import com.vasiu_catalina.beauty_salon.security.manager.EmployeeAuthenticationManager;
import com.vasiu_catalina.beauty_salon.security.service.SecurityService;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(SecurityConstants.class)
public class SecurityConfig {

	private final ClientAuthenticationManager clientAuthenticationManager;

	private final EmployeeAuthenticationManager employeeAuthenticationManager;

	private final SecurityService securityService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		ClientAuthenticationFilter clientAuthenticationFilter = new ClientAuthenticationFilter(
				clientAuthenticationManager, securityService);
		EmployeeAuthenticationFilter employeeAuthenticationFilter = new EmployeeAuthenticationFilter(
				employeeAuthenticationManager, securityService);

		JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter(securityService);

		clientAuthenticationFilter.setFilterProcessesUrl(securityService.getClientLoginPath());
		employeeAuthenticationFilter.setFilterProcessesUrl(securityService.getEmployeeLoginPath());

		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(requests -> requests
						.requestMatchers(HttpMethod.POST, securityService.getClientRegisterPath()).permitAll()
						.requestMatchers(HttpMethod.POST, securityService.getClientLoginPath()).permitAll()
						.requestMatchers(HttpMethod.POST, securityService.getEmployeeRegisterPath()).permitAll()
						.requestMatchers(HttpMethod.POST, securityService.getEmployeeLoginPath()).permitAll()
						.requestMatchers(HttpMethod.POST, securityService.getLogoutPath()).permitAll()
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(new ExceptionHandlerFilter(),
						UsernamePasswordAuthenticationFilter.class)
				.addFilter(clientAuthenticationFilter)
				.addFilter(employeeAuthenticationFilter)
				.addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(management -> management
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

}
