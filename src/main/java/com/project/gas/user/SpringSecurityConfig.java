package com.project.gas.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

  	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		  .csrf().disable()
		  .headers().frameOptions().sameOrigin()
		  .and()
			.authorizeRequests()
			.antMatchers("/", "/profile").permitAll()
			.antMatchers("/images/**", "/js/**", "/css/**").permitAll()
			.anyRequest().authenticated()
		  .and()
			.formLogin()
			.loginPage("/")
			.loginProcessingUrl("/login-Proc")
			.usernameParameter("id")
			.defaultSuccessUrl("/home")
			.permitAll()
		  .and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true);
		return http.build();
	}
 }