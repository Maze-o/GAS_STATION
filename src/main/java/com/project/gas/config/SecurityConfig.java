package com.project.gas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
//			.formLogin(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests((auth) 
			-> auth.requestMatchers("/", "/signup", "/signin", "/fav").permitAll()
			.anyRequest().authenticated())

				.formLogin(login -> 
				login.loginPage("/signin").loginProcessingUrl("/").usernameParameter("usernameXX").defaultSuccessUrl("/"))
				.logout((logout) -> logout.logoutSuccessUrl("/").invalidateHttpSession(true))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.build();
	}

}
