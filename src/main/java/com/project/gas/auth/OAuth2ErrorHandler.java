package com.project.gas.auth;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2ErrorHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {
		System.out.println("OAuth2ErrorHandler 호출됨");
		  // 실패 시 쿼리 스트링 추가하여 리다이렉트
        String redirectUrl = "/?oauth_error" + exception.getMessage(); // 실패 메시지를 쿼리 스트링에 포함
        response.sendRedirect(redirectUrl);

	}
}