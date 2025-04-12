package com.project.gas.user;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.gas.auth.PrincipalDetailsService;
import com.project.gas.dto.FindPwRequest;
import com.project.gas.dto.JoinRequest;
import com.project.gas.dto.LoginRequest;
import com.project.gas.dto.UpdateUserRequest;
import com.project.gas.dto.User;
import com.project.gas.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserService userService;
	private final JwtProvider jwtProvider;
	private final PrincipalDetailsService principalDetailsService;

	// jwt토큰의 유효기간이 끝났을 때 액세스토큰과 리프래시토큰을 발급
	@PostMapping("/auth/refresh")
	public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("Authorization") String refreshToken) {
		// "Bearer " 제거
		if (refreshToken.startsWith("Bearer ")) {
			refreshToken = refreshToken.substring(7);
		}

		try {
			// 토큰 갱신
			Map<String, String> newTokens = jwtProvider.renewTokens(refreshToken);
			return ResponseEntity.ok(newTokens);
		} catch (RuntimeException e) {
			return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
		}
	}

	@GetMapping("/login/oauth2/success")
	public ResponseEntity<String> validateToken(HttpServletRequest request) {
		// 요청에서 JWT 토큰 가져오기
		String jwtToken = null;
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JWT")) {
					jwtToken = cookie.getValue();
					break;
				}
			}
		}

		if (jwtToken == null) {
			return ResponseEntity.status(401).body("JWT 토큰이 없습니다.");
		}

		// 토큰의 사용자 이름 추출
		String username = jwtProvider.extractUsername(jwtToken);

		// 토큰 유효성 검사
		if (jwtProvider.isTokenValid(jwtToken, username)) {
			return ResponseEntity.ok("토큰이 유효합니다."); // 성공적으로 유효성 검사됨
		} else {
			return ResponseEntity.status(401).body("유효하지 않은 JWT 토큰입니다.");
		}
	}

	@GetMapping("/login")
	public String loginStepHandler(@RequestParam(value = "step", required = false) String step, Model model) {
		if ("PASSWORD_ENTRY".equals(step)) {
			// 비밀번호 입력 단계로 이동
			return "menus/loginEntryPage"; // 이메일과 비밀번호 입력 화면
		}
		// 그 외 기본 로그인 페이지
		return "menus/login";
	}

	@PostMapping("/login/authenticate")
	public ResponseEntity<Map<String, String>> signin(@ModelAttribute LoginRequest loginRequest) {
		try {
			// service에서 로그인 처리
			Map<String, String> response = userService.signin(loginRequest);
			return ResponseEntity.ok(response); // JWT를 사용자닉네임 포함 JSON 응답으로 반환
		} catch (IllegalArgumentException | UsernameNotFoundException | BadCredentialsException e) {
			System.out.println("실패 : " + e.getMessage());
			// 예외 발생 시 적절한 메시지를 포함하여 400 Bad Request 반환
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@GetMapping("/signup")
	
	public String signup() {
		return "menus/signup";
	}

	@PostMapping("/signup")
	public String signup(@ModelAttribute JoinRequest joinRequest) {
		userService.signup(joinRequest);
		System.out.println("joinRequest  : " + joinRequest);
		return "redirect:/";
	}

	@GetMapping("/fav")
	public String fav() {
		return "menus/fav";
	}

	@GetMapping("/findpw")
	public String findpw() {
		return "menus/findpw";
	}

	@PostMapping("/findpw")
	public ResponseEntity<Map<String, String>> recoverPassword(@RequestBody FindPwRequest findPwRequest) {
		try {
			String password = userService.findPw(findPwRequest); // 유저 비밀번호 찾기 (암호화 전의 비밀번호를 받아옴)
			return ResponseEntity.ok(Map.of("userpw", password)); // 암호화 하기 전의 비밀번호를 프론트로 전달
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage())); // 에러 발생시 프론트로 전달
		}

	}

	@GetMapping("/changepw")
	public String changepw() {
		return "menus/changepw";
	}

	@GetMapping("/checkpw")
	public String checkpw() {
		return "menus/checkpw";
	}

	@PostMapping("/checkpw")
	public ResponseEntity<Map<String, Object>> checkpw(@RequestBody User userpw) {
		// DB에 저장된 패스워드 가져오기
		String pw = userpw.getUserpw();
		
		// 유저 정보 확인하고 있으면 가져옴
		Optional<User> user = userService.getCurrentUser();
		
		// 비밀번호 확인 결과를 리턴해줘야 하기 때문에 map 사용
		Map<String, Object> map = new HashMap<>();

		// 사용자가 존재하지 않을 경우
		if (!user.isPresent()) {
			throw new IllegalArgumentException("사용자를 찾을 수 없습니다."); 
		}

		// 비밀번호가 빈값인지 확인
		if (pw == null || pw.trim().isEmpty()) {
			throw new IllegalArgumentException("비밀번호는 필수입니다");
		}

		// 비밀번호 검증 (이곳에서 비밀번호 불일치 예외가 발생할 수 있음)
		userService.verifyPassword(pw, user.get());

		map.put("success", true);
		return ResponseEntity.ok(map);

	}

	@GetMapping("/updateInfo")
	public String updateinfo() {
		return "menus/updateInfo";
	}

	@PatchMapping("/updateInfo")
	public ResponseEntity<Map<String, String>> updateinfo(@RequestHeader(value = "Authorization") String token,
			@RequestBody UpdateUserRequest updateRequest) {

		// 토큰에서 username 추출
		String jwtToken = token.replace("Bearer ", "");
		String username = jwtProvider.extractUsername(jwtToken);

		userService.updateUser(username, updateRequest);

		Map<String, String> map = new HashMap<>();
		map.put("success", "사용자 정보 업데이트가 완료됐습니다!");

		return ResponseEntity.ok(map);

	}

}
