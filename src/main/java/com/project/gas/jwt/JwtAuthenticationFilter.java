package com.project.gas.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.gas.auth.PrincipalDetails;
import com.project.gas.user.UserRepository;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider; // JWT 유틸리티 클래스
	private final UserDetailsService userDetailsService; // 사용자 세부 정보 서비스를 위한 인터페이스

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// 요청 헤더에서 Authorization 정보를 가져옴
		final String authHeader = request.getHeader("Authorization");
		// JWT 토큰을 저장할 변수
		String jwt = null;
		// 사용자 이름을 저장할 변수
		String username = null;



		// OAuth2 인증 요청은 필터를 통과
		if (request.getRequestURI().contains("/oauth2/authorization/")
				|| request.getRequestURI().contains("/login/oauth2/code/")) {
//			System.out.println("헤더 정보 : " + request.getHeader("Authorization"));
//			System.out.println("OAUTH2 요청 처리중 (jwtFilter)");
//			System.out.println("현재 SecurityContext에 인증 정보: " + SecurityContextHolder.getContext().getAuthentication());
			chain.doFilter(request, response);
			return; // OAuth2 인증 요청은 필터를 통과시킴
		}

		// Authorization 헤더가 없거나 Bearer로 시작하지 않으면 다음 필터로 진행
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			chain.doFilter(request, response); // 다음 필터로 요청을 전달
			return;
		}

		// JWT 토큰을 추출
		jwt = authHeader.substring(7); // "Bearer " 다음의 토큰을 가져옴
		username = jwtProvider.extractUsername(jwt); // JWT에서 사용자 이름 추출

		// 사용자 이름이 null이 아니고, 현재 SecurityContext에 인증 정보가 없을 때
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			// UserDetails를 PrincipalDetails로 캐스팅
			PrincipalDetails principalDetails = (PrincipalDetails) userDetailsService.loadUserByUsername(username);

			// JWT 토큰이 유효한지 확인
			if (jwtProvider.isTokenValid(jwt, principalDetails.getUsername())) {
				// 유효한 토큰이라면, UsernamePasswordAuthenticationToken 생성
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						principalDetails, null, principalDetails.getAuthorities());
				// 요청의 세부 정보를 설정
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// SecurityContext에 인증 정보 저장
				SecurityContextHolder.getContext().setAuthentication(authToken);
			} else {
                // JWT가 유효하지 않을 때 처리
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 정보가 유효하지 않습니다");
                return;
            }
		}

		// 다음 필터로 요청을 전달
		chain.doFilter(request, response);
	}

}