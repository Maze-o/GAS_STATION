package com.project.gas.user;

<<<<<<< HEAD
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
=======
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc
import org.springframework.web.bind.annotation.PostMapping;

import com.project.gas.dto.JoinRequest;
import com.project.gas.dto.LoginRequest;
<<<<<<< HEAD
import com.project.gas.jwt.JwtUtil;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/signin")
	public String signin() {
		// 현재 사용자의 인증 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("1" + authentication);
		System.out.println("2" + authentication.isAuthenticated());
		System.out.println("3" + authentication.getPrincipal());

		// 인증 정보가 있고, 인증이 되어 있으며, 익명의 사용자가 아닌 경우
		if (authentication != null && authentication.isAuthenticated()
				&& !(authentication.getPrincipal() instanceof String
						&& "anonymousUser".equals(authentication.getPrincipal()))) {
			return "redirect:/"; // 이미 로그인된 경우 메인 페이지로 리다이렉트
		}

		return "menus/signin"; // 로그인되지 않은 경우 로그인 페이지 반환
	}

	@PostMapping("/signin")
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

=======
import com.project.gas.dto.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserService userService;

	/*
	 * @GetMapping("/signin") public String sign_in() { return "/menus/sign_in"; }
	 */
	
//    @GetMapping(value = {"", "/"})
//    public String home(Model model) {
//        
//        model.addAttribute("loginType", "security-login");
//        model.addAttribute("pageName", "스프링 시큐리티 로그인");
//
//        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
//        GrantedAuthority auth = iter.next();
//        String role = auth.getAuthority();
//
//        User loginMember = userService.getLoginMemberById(loginId);
//
//        if (loginMember != null) {
//            model.addAttribute("name", loginMember.getUsername());
//        }
//
//        return "home";
//    }

	@GetMapping("/signin")
	public String signin(Model model) {
		System.out.println("로그인");
//		System.out.println("controller : " + re.getUserid());

//		model.addAttribute("loginType", "session-login");
//		model.addAttribute("pageName", "세션 로그인");
//
//		model.addAttribute("loginRequest", new LoginRequest());
		return "menus/signin";
	}

	@PostMapping("/signin")
	public String signin(LoginRequest loginRequest, Model model, BindingResult bindingResult) {
		System.out.println("post 로그인 시도");

		User user =  userService.signin(loginRequest);
		
		System.out.println("userID : " + loginRequest.getUserid());
		System.out.println("userPW : " + loginRequest.getUserpw());

//		model.addAttribute("loginType", "session-login");
//		model.addAttribute("pageName", "세션 로그인");

		if (user == null) {
			bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
			System.out.println("틀림");
			return "menus/signin";
		} else { 
			System.out.println("성공");
			return "redirect:/";
		}
		
>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc
	}

	@GetMapping("logout")
	public String signout(HttpSession session) {
<<<<<<< HEAD
//		session.removeAttribute("user");
=======
		session.removeAttribute("user");
>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc
		return "redirect:/";
	}

	@GetMapping("/signup")
	public String signup() {
		return "menus/signup";
	}

	@PostMapping("/signup")
<<<<<<< HEAD
	public String signup(@ModelAttribute JoinRequest joinRequest) {

		userService.signup(joinRequest);
=======
	public String singup(JoinRequest joinRequest) {

		userService.securityJoin(joinRequest);

>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc
//		User userEntity = userservice.signup(user);
		System.out.println("joinRequest  : " + joinRequest);

		return "redirect:/";
	}

	@GetMapping("/fav")
	public String fav() {
		return "menus/fav";
	}

	@GetMapping("/findpw")
	public String findpw() {
		return "menus/find_pw";
	}

}
