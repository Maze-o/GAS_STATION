package com.project.gas.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

//	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 비밀 키 생성
	private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간 유효
	private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7일 유효
	private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes()); // 고정된 비밀 키 설정
    }
	// 토큰 생성 메서드
	public String generateToken(String username) {
		// 토큰 생성 시 현재 시간과 만료 시간을 설정
		String uniqueIdentifier = String.valueOf(System.currentTimeMillis());
		Date now = new Date();
		Date expiryDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

		return Jwts.builder()
				.setSubject(username) // 사용자 이름 설정
				.setIssuedAt(now) // 발급 시간 설정
				.setExpiration(expiryDate) // 만료 시간 설정
				.claim("uniqueId", uniqueIdentifier) // 유니크한 값 추가
				.signWith(key) // 비밀 키로 서명
				.compact(); // JWT 문자열 생성
	}
	

	// 리프레시 토큰 생성 메서드
	public String generateRefreshToken(String username) {
		Date now = new Date();
		Date expiryDate = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME);

		return Jwts.builder().setSubject(username)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(key)
				.compact();
	}

	// 토큰에서 사용자 이름 추출
	public String extractUsername(String token) {
		return extractClaims(token).getSubject(); // Claims에서 사용자 이름 반환
	}

	// 토큰 유효성 확인
	public boolean isTokenValid(String token, String username) {
		return (username.equals(extractUsername(token)) && !isTokenExpired(token)); // 사용자 이름과 만료 여부 확인
	}

	// 토큰 만료 여부 확인
	private boolean isTokenExpired(String token) {
		return extractClaims(token).getExpiration().before(new Date()); // 현재 시간보다 만료 시간이 이전인지 확인
	}

	// Claims 추출 메서드
	private Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key) // 비밀 키로 검증
				.build().parseClaimsJws(token).getBody(); // Claims 반환
	}
}
