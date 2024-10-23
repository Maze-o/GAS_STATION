package com.project.gas.user;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.gas.dto.FindPwRequest;
import com.project.gas.dto.JoinRequest;
import com.project.gas.dto.LoginRequest;
import com.project.gas.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserService userService;
	private final JwtProvider jwtProvider;

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

//	@GetMapping("/login")
//	public String signin(Model model, Principal principal,
//			@RequestParam(value = "error", required = false) String error) {
//		// 현재 사용자의 인증 정보 가져오기
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//		// 인증 정보가 있고, 인증이 되어 있으며, 익명의 사용자가 아닌 경우
//		if (authentication != null && authentication.isAuthenticated()
//				&& !(authentication.getPrincipal() instanceof String
//						&& "anonymousUser".equals(authentication.getPrincipal()))) {
//			return "redirect:/"; // 이미 로그인된 경우 메인 페이지로 리디렉트
//		}
//
//		// 로그인되지 않은 경우 로그인 페이지 반환
//		return "menus/login";
//	}

	@PostMapping("/login/authenticate")
	public ResponseEntity<Map<String, String>> signin(@ModelAttribute LoginRequest loginRequest) {
		try {
			// service에서 로그인 처리
			Map<String, String> response = userService.signin(loginRequest);
			System.out.println("token  : " + response);
			return ResponseEntity.ok(response); // JWT를 사용자닉네임 포함 JSON 응답으로 반환
		} catch (IllegalArgumentException | UsernameNotFoundException | BadCredentialsException e) {
			System.out.println("실패 : " + e.getMessage());
			// 예외 발생 시 적절한 메시지를 포함하여 400 Bad Request 반환
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@GetMapping("logout")
	public String signout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:/";
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
			System.out.println("성공" + password);
			return ResponseEntity.ok(Map.of("userpw", password )); // 암호화 하기 전의 비밀번호를 프론트로 전달
		} catch (Exception e) {	
			System.out.println("에러 발생" + e.getMessage());	
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage())); // 에러 발생시 프론트로 전달
		}
		
	}
	
	@GetMapping("/changepw")
	public String changepw()  {
		return "menus/changepw";
	}

}
