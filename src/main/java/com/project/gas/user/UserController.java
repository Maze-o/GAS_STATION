package com.project.gas.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {


	private final AuthService authservice;
	

	/*
	 * @GetMapping("/signin") public String sign_in() { return "/menus/sign_in"; }
	 */

	@GetMapping("/signin")
	public String sign_in() {
		System.out.println("로그인");
		return "menus/sign_in";
	}

	
//	 @PostMapping("/signin")
//	 public String signin() {
//		 System.out.println("로그인됐다!!!!!");
//		 
//		 return "redirect:/";
//	 }
//	


	
	
	
	
	@GetMapping("logout")
	public String signout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:/";
	}

	@GetMapping("/signup")
	public String sign_up() {
		return "menus/sign_up";
	}
	
	@PostMapping("/signup")
	public String sing_up(SignUpDTO signupDto) {
		User user = signupDto.toEntity();
		
		User userEntity = authservice.signup(user);
		System.out.println(userEntity);
		
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
