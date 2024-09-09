package com.project.gas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration

//해주지 않으니 비밀번호 암호화의 bean이 userService의 BCryptPasswordEncoder랑 무한루프가 돼서 security config의 빈을 기본으로 잡아줌
@EnableWebSecurity 
public class SecurityConfig {



	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth.requestMatchers("/", "/signup", "/signin", "/fav").permitAll()
						.anyRequest().authenticated())

				.formLogin(login -> login.loginPage("/signin").loginProcessingUrl("/").usernameParameter("username")
						.passwordParameter("userpw").defaultSuccessUrl("/signin").failureUrl("/signin").permitAll())

				.logout((logout) -> logout.logoutSuccessUrl("/").invalidateHttpSession(true))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}
	
	// 비밀번호 암호화 
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}



}
