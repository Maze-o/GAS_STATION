package com.project.gas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.gas.auth.CustomOAuth2UserService;
import com.project.gas.auth.OAuth2ErrorHandler;
import com.project.gas.auth.OAuth2SuccessHandler;
import com.project.gas.auth.PrincipalDetailsService;
import com.project.gas.jwt.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
//해주지 않으니 비밀번호 암호화의 bean이 userService의 BCryptPasswordEncoder랑 무한루프가 돼서 security config의 빈을 기본으로 잡아줌
@EnableWebSecurity
public class SecurityConfig {

	private final CustomOAuth2UserService oAuth2UserService;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final PrincipalDetailsService principalDetailsService;
	private final OAuth2ErrorHandler oAuth2ErrorHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.formLogin(AbstractHttpConfigurer::disable) // 폼로그인 비활성화 (jwt사용하기 위해)
				.authorizeHttpRequests(auth -> auth
//						.requestMatchers("/signup", "/signin", "/fav", "/signin/oauth2/success",
//								"signin/oauth2/authorization/*", "/signin/oauth2/**", "/signin/oauth2/code/google").permitAll()
						/* .requestMatchers("/signin").denyAll() */
						.requestMatchers("/**").permitAll()
//						.requestMatchers("/?oauth_success=true").denyAll()
						.anyRequest().authenticated())

				// OAuth2 사용자 정보를 처리할 서비스 설정
				.oauth2Login(oauth2 -> {
					oauth2.loginPage("/login").userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
							.successHandler(oAuth2SuccessHandler).failureHandler(oAuth2ErrorHandler);

				})

				.logout((logout) -> logout.logoutSuccessUrl("/").invalidateHttpSession(true))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// 필터 (Jwt)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//				.addFilterAfter(new CustomAccessFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(jwtAuthFilter, OAuth2LoginAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("http://localhost:8080"); // 필요한 도메인 추가
		configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
		configuration.addAllowedHeader("*"); // 모든 헤더 허용
		configuration.setAllowCredentials(true); // 쿠키 허용

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	// 정적 리소스에 대한 접근을 인증 없이 허용 (security를 적용하지 않을 리소스)
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/error",
				"/favicon.ico");

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