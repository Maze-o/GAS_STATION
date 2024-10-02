package com.project.gas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.project.gas.config.auth.PrincipalDetailsService;
import com.project.gas.jwt.JwtAuthenticationFilter;

@Configuration

//해주지 않으니 비밀번호 암호화의 bean이 userService의 BCryptPasswordEncoder랑 무한루프가 돼서 security config의 빈을 기본으로 잡아줌
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthFilter;

	private final PrincipalDetailsService principalDetailsService;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, PrincipalDetailsService principalDetailsService) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.principalDetailsService = principalDetailsService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {	
		http.csrf(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable)
//				.formLogin(AbstractHttpConfigurer::disable) // 폼로그인 비활성화 (jwt사용하기 위해)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/signup", "/signin", "/fav", "/css/**", "/images/**", "/js/**").permitAll()
						.requestMatchers("/signin").denyAll()
						.anyRequest().authenticated())
//				.formLogin(login -> login.loginPage("/signin").loginProcessingUrl("/signin").usernameParameter("userid")
//						.passwordParameter("userpw").defaultSuccessUrl("/").failureUrl("/signin").permitAll())
//				.oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/"))
				.logout((logout) -> logout.logoutSuccessUrl("/").invalidateHttpSession(true))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터
																								// 추가
		return http.build();
	}

	// 비밀번호 암호화
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(principalDetailsService)
				.passwordEncoder(bCryptPasswordEncoder());
		return authenticationManagerBuilder.build();
	}

}