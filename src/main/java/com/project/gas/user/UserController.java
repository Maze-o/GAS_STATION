package com.project.gas.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	/*
	 * @GetMapping("/signin") public String sign_in() { return "/menus/sign_in"; }
	 */

	@GetMapping("/signin")
	public String sign_in() {
		System.out.println("로그인");
		return "/menus/sign_in";
	}

	/*
	 * @PostMapping("/login") public String signin(String userId, String userPw,
	 * HttpSession session, Model model) { User user =
	 * userService.findByUserId(userId); System.out.println("1"); if (user != null
	 * && user.equals(userPw)) { System.out.println("2");
	 * session.setAttribute("user", user);
	 * 
	 * return "redirect:/"; } else { model.addAttribute("아이디 또는 비밀번호가 일치하지 않습니다");
	 * return "redirect:/"; } }
	 */
	
	@PostMapping("/login")
	public String login(@ModelAttribute User userId) {
		if (userService.findByUserId(userId)) {
			return "redirect:/";
		}
		return "login";
		
	}
	
	
	
	
	@GetMapping("logout")
	public String signout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:/";
	}

	@GetMapping("/signup")
	public String sign_up() {
		return "/menus/sign_up";
	}

	@GetMapping("/fav")
	public String fav() {
		return "/menus/fav";
	}

	@GetMapping("/findpw")
	public String findpw() {
		return "/menus/find_pw";
	}

}
