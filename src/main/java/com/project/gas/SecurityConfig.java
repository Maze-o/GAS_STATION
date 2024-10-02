package com.project.gas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
<<<<<<< HEAD
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
=======
>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
<<<<<<< HEAD
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.gas.config.auth.PrincipalDetailsService;
import com.project.gas.jwt.JwtAuthenticationFilter;
import com.project.gas.user.UserService;
=======

>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc

@Configuration

//해주지 않으니 비밀번호 암호화의 bean이 userService의 BCryptPasswordEncoder랑 무한루프가 돼서 security config의 빈을 기본으로 잡아줌
<<<<<<< HEAD
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
=======
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
>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

<<<<<<< HEAD
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(principalDetailsService)
				.passwordEncoder(bCryptPasswordEncoder());
		return authenticationManagerBuilder.build();
	}
=======

>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc

}
