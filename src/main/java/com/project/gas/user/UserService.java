package com.project.gas.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.gas.dto.JoinRequest;
import com.project.gas.dto.LoginRequest;
import com.project.gas.dto.User;
import com.project.gas.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepo;
	private final BCryptPasswordEncoder encoder;
	private final JwtUtil jwtUtil;
	private final AuthenticationManager authenticationManager;

	// 아이디 중복 체크
	public boolean checkLoginIdDuplicate(String userid) {
		return userRepo.existsByuserid(userid);
	}

	// 유저 아이디로 사용자 찾기
	private Optional<User> findUserByUsername(String username) {
		return userRepo.findByuserid(username);
	}

	// 회원가입 서비스 처리
	public void signup(JoinRequest joinRequest) {
		validateSignupRequest(joinRequest); // 회원가입 시도 시 처리할 로직
		securityJoin(joinRequest); // 비밀번호 암호화 처리 후 저장
	}

	// 로그인 서비스 처리
	public Map<String, String> signin(LoginRequest loginRequest) {
		// 아이디가 빈칸일 경우
		if (loginRequest.getUserid() == null || loginRequest.getUserid().isEmpty()) {
			throw new IllegalArgumentException("아이디는 필수입니다.");
		}

		// 비밀번호가 빈칸일 경우
		if (loginRequest.getUserpw() == null || loginRequest.getUserpw().isEmpty()) {
			throw new IllegalArgumentException("비밀번호는 필수입니다.");
		}

		// 아이디가 DB에 저장된 정보랑 틀릴 경우
		User user = findUserByUsername(loginRequest.getUserid())
				.orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다."));

		// 비밀번호가 DB에 저장된 정보랑 틀릴 경우 (비밀번호 암호화를 진행시키고 대조해야함)
		if (!verifyPassword(loginRequest.getUserpw(), user)) {
			throw new BadCredentialsException("비밀번호가 틀립니다.");
		}

		// JWT 생성
		String token = jwtUtil.generateToken(user.getUserid());

		// 유저의 닉네임을 뷰에 담기 위해서 Map에 토큰정보와 유저네임을 키벨류형태로 저
		Map<String, String> response = new HashMap<>();
		response.put("token", token);
		response.put("username", user.getUsername());

		return response;
	}

	// 비밀번호 검증
	private boolean verifyPassword(String rawPassword, User user) {
		return encoder.matches(rawPassword, user.getUserpw());
	}

	public User getLoginMemberById(Long userid) {
		return userRepo.findById(userid).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다.")); // 예외 처리
	}

	// 회원가입 비밀번호 암호화 처리
	private void securityJoin(JoinRequest joinRequest) {
		joinRequest.setUserpw(encoder.encode(joinRequest.getUserpw()));
		userRepo.save(joinRequest.toEntity());
	}

	// 회원가입 요청 검증
	private void validateSignupRequest(JoinRequest joinRequest) {
		if (userRepo.existsByuserid(joinRequest.getUserid())) {
			throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
		}

		if (joinRequest.getUserpw() == null || joinRequest.getUserpw().isEmpty()) {
			throw new IllegalArgumentException("비밀번호는 필수입니다.");
		}
	}
}
