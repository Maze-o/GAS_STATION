//package com.project.gas.filter;
//
//import java.io.IOException;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class CustomAccessFilter extends OncePerRequestFilter {
//
//	@Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//		// 요청 URL 로깅
//		System.out.println("Requested URL: " + request.getRequestURL());
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        // 인증된 사용자이면서 쿼리 스트링에 oauth_success=true가 포함되어 있는 경우 접근 차단
//        if (authentication != null && authentication.isAuthenticated() && request.getParameter("oauth_success") != null) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
//            System.out.println("CustomAccessFilter에서 접근 차단");
//            return;	
//        }
//
//        // 필터 체인의 다음 필터로 이동
//        chain.doFilter(request, response);
//    }
//}