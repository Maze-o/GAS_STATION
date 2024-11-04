package com.project.gas.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.gas.auth.PrincipalDetails;
import com.project.gas.auth.PrincipalDetailsService;
import com.project.gas.dto.FindPwRequest;
import com.project.gas.dto.JoinRequest;
import com.project.gas.dto.LoginRequest;
import com.project.gas.dto.UpdateUserRequest;
import com.project.gas.dto.User;
import com.project.gas.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	// 로그인 유효성 검사에 대한 상수들
	private static final int USERID_MIN_LENGTH = 4; // 아이디 최소 길이
	private static final int USERID_MAX_LENGTH = 12; // 아이디 최대 길이
	private static final int USERPW_MIN_LENGTH = 6; // 비밀번호 최소 길이
	private static final int USERPW_MAX_LENGTH = 20; // 비밀번호 최대 길이
	private static final int USERNAME_MIN_LENGTH = 3; // 이름 최소 길이
	private static final int USERNAME_MAX_LENGTH = 10; // 이름 최대 길이

	private final UserRepository userRepo;
	private final BCryptPasswordEncoder encoder;
	private final JwtProvider jwtProvider;
	private final PrincipalDetailsService principalDetailsService;

	// 아이디 중복 체크
	public boolean checkLoginIdDuplicate(String userid) {
		return userRepo.existsByuserid(userid);
	}

	// 닉네임 중복 체크
	public boolean checkNicknameDuplicate(String username) {
		return userRepo.existsByUsername(username);
	}

	// 유저 아이디로 사용자 찾기
//	private Optional<User> findUserByUsername(String username) {
//		return userRepo.findByuserid(username);
//	}

	// 회원가입 서비스 처리
	public User signup(JoinRequest joinRequest) {
		validateSignupRequest(joinRequest);

		User newUser = new User();
		newUser.setUserid(joinRequest.getUserid());
		newUser.setUsername(joinRequest.getUsername());
		newUser.setUserpw(encoder.encode(joinRequest.getUserpw()));

		return userRepo.save(newUser);
	}

	// 회원가입 요청 검증
	private void validateSignupRequest(JoinRequest joinRequest) {
		// 아이디와 비밀번호 유효성 검사
		validateUserIdAndPassword(joinRequest.getUserid(), joinRequest.getUserpw());

		// 아이디 중복 검사
		if (checkLoginIdDuplicate(joinRequest.getUserid())) {
			throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
		}

		// 닉네임 중복 검사
		if (checkNicknameDuplicate(joinRequest.getUsername())) {
			throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
		}
	}

	// 로그인 서비스 처리
	public Map<String, String> signin(LoginRequest loginRequest) {
		// 빈칸 검사
		validateUserIdAndPassword(loginRequest.getUserid(), loginRequest.getUserpw());

		// PrincipalDetailsService를 사용하여 사용자 정보 가져오기
		UserDetails userDetails = principalDetailsService.loadUserByUsername(loginRequest.getUserid());
		User user = ((PrincipalDetails) userDetails).getUser(); // PrincipalDetails에서 User 객체를 가져옴

		// 비밀번호 암호화 해서 검사
		if (!verifyPassword(loginRequest.getUserpw(), user)) {
			throw new BadCredentialsException("비밀번호가 틀립니다.");
		}

		// 토큰 생성
		// 액세스 토큰과 리프레시 토큰 생성
		String accessToken = null;
		String refreshToken = null;
		if (loginRequest.isRememberMe()) {
			// 로그인 유지 체크박스를 선택한 경우
			accessToken = jwtProvider.generateToken(user, 3600); // 1시간
			refreshToken = jwtProvider.generateRefreshToken(user, 604800); // 1주일
		} else {
			// 로그인 유지 체크박스를 선택하지 않은 경우
			accessToken = jwtProvider.generateToken(user, 3600); // 1시간
			refreshToken = jwtProvider.generateRefreshToken(user, 3600); // 1시간
		}

		Map<String, String> response = new HashMap<>();
		response.put("accessToken", accessToken);
		response.put("refreshToken", refreshToken);
		response.put("username", user.getUsername());

		return response;
	}

	// 아이디와 비밀번호 빈칸 체크 및 길이 체크
	private void validateUserIdAndPassword(String userid, String userpw) {
		// 아이디가 빈칸일 경우 (trim, isEmpty함수를 사용하는 이유는 빈 칸 ("") 이거나 공백(" ") 일 경우를 감지하기 위해)
		if (userid == null || userid.trim().isEmpty()) {
			throw new IllegalArgumentException("아이디는 필수입니다.");
		}

		// 아이디 길이 검사
		if (userid.length() < USERID_MIN_LENGTH || userid.length() > USERID_MAX_LENGTH) {
			throw new IllegalArgumentException(
					"아이디는 " + USERID_MIN_LENGTH + "자 이상 " + USERID_MAX_LENGTH + "자 이하이어야 합니다.");
		}

		// 비밀번호가 빈칸인 경우
		if (userpw == null || userpw.trim().isEmpty()) {
			throw new IllegalArgumentException("비밀번호는 필수입니다.");
		}

		// 비밀번호 길이 검사
		if (userpw.length() < USERPW_MIN_LENGTH || userpw.length() > USERPW_MAX_LENGTH) {
			throw new IllegalArgumentException(
					"비밀번호는 " + USERPW_MIN_LENGTH + "자 이상 " + USERPW_MAX_LENGTH + "자 이하이어야 합니다.");
		}
	}

	// 로그인 시 비밀번호 검사
	public boolean verifyPassword(String password, User user) {
		boolean matches = encoder.matches(password, user.getUserpw());
		if (!matches) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		return matches;
	}

	// 비밀번호 찾기 로직
	public String findPw(FindPwRequest findPwRequest) {
		// 조건에 맞는 사용자 조회
		User user = userRepo.findByUseridAndUsername(findPwRequest.getUserid(), findPwRequest.getUsername())
				.orElseThrow(() -> new RuntimeException("일치하는 사용자 정보가 없습니다."));

		// 임시 비밀번호 생성
		String tempPw = generateRandomPw();

		String encryptedPassword = encoder.encode(tempPw); // 여기에 비밀번호 암호화 로직 추가
		// 비밀번호 암호화

		// DB에 저장
		user.setUserpw(encryptedPassword);
		userRepo.save(user);

		return tempPw;
	}

	// 랜덤 비밀번호 생성
	private String generateRandomPw() {
		// UUID를 생성하고 문자열로 변환 후 처음 8자리 반환
		return UUID.randomUUID().toString().substring(0, 8);
	}

	// 사용자의 정보 가져오기
	public Optional<User> getCurrentUser() {
		// 인증이 된 상태라면 true, 안된 상태면 false가 할당된다
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);
		if (authentication != null && authentication.isAuthenticated()) {
			String userid = authentication.getName(); // 현재 사용자의 userid를 가져옴
			// 유저의 정보를 반환
			return userRepo.findByuserid(userid);
		}
		return Optional.empty(); // 인증된 사용자가 없을 경우 빈 Optional 반환
	}

	// 사용자 정보 업데이트 (dirty checking이기 때문에 save함수 호출 필요 없다)
	@Transactional
	public void updateUser(String userid, UpdateUserRequest updateRequest) {
		// 사용자 정보 가져오기
		User user = userRepo.findByuserid(userid).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

		String username = updateRequest.getUsername();
		String userpw = updateRequest.getUserpw();

		// 둘 다 null인 경우 예외 처리 (javascript 비활성화 대비)
		if (username == null && userpw == null) {
			throw new IllegalArgumentException("최소 하나의 정보는 변경해야 합니다.");
		}

		// 닉네임 유효성 검사
		if (username != null && !username.trim().isEmpty()) {
			if (username.length() < USERNAME_MIN_LENGTH || username.length() > USERNAME_MAX_LENGTH) {
				throw new IllegalArgumentException(
						"닉네임은 " + USERNAME_MIN_LENGTH + "자 이상 " + USERNAME_MAX_LENGTH + "자 이하이어야 합니다.");
			}
			user.setUsername(username);
		}

		// 비밀번호가 null 이 아닐 때만 업데이트 (username만 변경 할 수 있도록)
		if (userpw != null && !userpw.trim().isEmpty()) {
			// 비밀번호 유효성 검사
			if (userpw.length() < USERPW_MIN_LENGTH || userpw.length() > USERPW_MAX_LENGTH) {
				throw new IllegalArgumentException(
						"비밀번호는 " + USERPW_MIN_LENGTH + "자 이상 " + USERPW_MAX_LENGTH + "자 이하이어야 합니다.");
			}
			// 암호화 후 저장
			user.setUserpw(encoder.encode(userpw));
		}

	}
}
