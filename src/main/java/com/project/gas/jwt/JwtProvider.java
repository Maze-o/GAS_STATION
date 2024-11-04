package com.project.gas.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.project.gas.dto.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

//	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 비밀 키 생성
//	private static final long EXPIRATION_TIME = 1000 * 60 * 5; // 10분 유효
//	private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7일 유효
	private final Key key;

	public JwtProvider(@Value("${jwt.secret}") String secretKey) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes()); // 고정된 비밀 키 설정
	}

	// 토큰 생성 메서드
	public String generateToken(User user, int expireDate) {
		long expiryDurationInMillis = expireDate * 1000; // expireDate를 밀리초로 변환

		Date now = new Date();
		Date expiryDate = new Date(System.currentTimeMillis() + expiryDurationInMillis);

		return Jwts.builder()
				.setSubject(user.getUserid()) // 사용자 이름 설정
				.claim("username", user.getUsername())
				.setIssuedAt(now) // 발급 시간 설정
				.setExpiration(expiryDate) // 만료 시간 설정
				.signWith(key) // 비밀 키로 서명
				.compact(); // JWT 문자열 생성
	}

	// 리프레시 토큰 생성 메서드
	public String generateRefreshToken(User user, int expireDate) {
		long expiryDurationInMillis = expireDate * 1000; // expireDate를 밀리초로 변환
		Date now = new Date();
		Date expiryDate = new Date(System.currentTimeMillis() + expiryDurationInMillis);

		return Jwts.builder()
				.setSubject(user.getUserid())
				.claim("username", user.getUsername())
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(key)
				.compact();
	}

	// 토큰에서 사용자 이름 추출
	public String extractUsername(String token) {
		System.out.println("토큰 사용자 이름 추출 : " + extractClaims(token).getSubject());
		return extractClaims(token).getSubject(); // Claims에서 사용자 이름 반환
	}

	// 토큰 유효성 확인
	public boolean isTokenValid(String token, String username) {
		try {
			return username.equals(extractUsername(token)) && !isTokenExpired(token);
		} catch (ExpiredJwtException e) {
			return false;
		}
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

	// 액세스 토큰 및 리프레시 토큰 재발급 메서드
	public Map<String, String> renewTokens(String refreshToken) {
		try {
			if (isTokenExpired(refreshToken)) {
				throw new RuntimeException("로그인 정보가 만료되었습니다. 로그아웃 후 다시 로그인해주세요.");
			}
			
			// 리프레시 토큰이 유효하면 토큰에 저장된 사용자 정보로 새로운 액세스 토큰 발급
            Claims claims = extractClaims(refreshToken);
            String userid = claims.getSubject(); // subject에서 userid추출
            String username = claims.get("nickname", String.class);
            
            User user = new User();
            user.setUserid(userid);
            user.setUsername(username);
            
            String newAccessToken = generateToken(user, 3600);

			// 새로운 토큰들을 Map에 담아 반환
			Map<String, String> tokens = new HashMap<>();
			tokens.put("accessToken", newAccessToken);
			return tokens;

		} catch (ExpiredJwtException e) {
			throw new RuntimeException("로그인 정보가 만료되었습니다. 로그아웃 후 다시 로그인해주세요.");
		}
	}
}
