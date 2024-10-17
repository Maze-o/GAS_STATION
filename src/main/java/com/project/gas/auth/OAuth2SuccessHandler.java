//package com.project.gas.auth;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.gas.jwt.JwtProvider;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//
//@Component
//@AllArgsConstructor
//public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
//
//	private final JwtProvider jwtProvider;
//	private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 응답을 위한 ObjectMapper
//
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//
//		// OAuth2 인증된 사용자 정보 가져오기
//		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//		String username = oAuth2User.getAttribute("name"); // 사용자 이름 또는 ID
//
//		// JWT 토큰 생성
//		String jwtToken = jwtProvider.generateToken(username);
//
//		// 응답으로 보낼 데이터 설정
//		Map<String, String> responseData = new HashMap<>();
//		responseData.put("token", jwtToken); // JWT 토큰을 응답에 추가
//		response.setHeader("Authorization", "Bearer " + jwtToken);
//		System.out.println("token : " + jwtToken);
//		responseData.put("message", "로그인 성공");
////		responseData.put("redirectUrl", "/login/oauth2/success"); // redirecturl 추가
//
//
//		// JSON 응답 설정
//		response.setContentType("application/json");
//		response.setCharacterEncoding("UTF-8");
////		response.setStatus(HttpServletResponse.SC_OK); // 200 OK 상태 코드 설정
//
//		// JSON 형태로 JWT 토큰과 redirectUrl 응답
//		response.getWriter().write(objectMapper.writeValueAsString(responseData));
////		response.getWriter().flush();
////		String redirectUrl = "/login/oauth2/success?token=" + jwtToken;
//
////		response.sendRedirect(redirectUrl);
//	}
//
//}

package com.project.gas.auth;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.project.gas.jwt.JwtProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // OAuth2 인증된 사용자 정보 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("User attributes: " + oAuth2User.getAttributes());
        Map<String, Object> attributes = oAuth2User.getAttributes();
//        String username = oAuth2User.getAttribute("name"); // 사용자 이름 또는 ID

        // Naver의 응답 구조에서 nickname 가져오기
        String username = null;

        // Naver에서 사용자 정보 추출
        if (attributes.containsKey("response")) {
            Map<String, Object> responseAttributes = (Map<String, Object>) attributes.get("response");
            username = (String) responseAttributes.get("nickname"); // Naver의 nickname
        }

        // Google에서는 name 속성에서 이름을 가져옵니다.
        if (username == null) {
            username = (String) attributes.get("name"); // Google의 name
        }
        

        // JWT 토큰 생성
        String jwtToken = jwtProvider.generateToken(username);
        addCookie(response, "JWT", jwtToken, false, 60 * 60); // JWT 쿠키 추가
        addCookie(response, "username", username, false, 60 * 60); // 사용자 이름 쿠키 추가

        response.setCharacterEncoding("UTF-8");
        response.sendRedirect("/?oauth_success=true");
    }
    
 // 쿠키 추가 메서드
    private void addCookie(HttpServletResponse response, String name, String value, boolean httpOnly, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly); // httpOnly 설정
        cookie.setSecure(false); // 개발 시에는 false로 설정
        cookie.setPath("/"); // 모든 경로에서 쿠키가 사용될 수 있도록 설정
        cookie.setMaxAge(maxAge); // 쿠키의 유효 기간 설정
        response.addCookie(cookie); // 쿠키 추가
    }
}

