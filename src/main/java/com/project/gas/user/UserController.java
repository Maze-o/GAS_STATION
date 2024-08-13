package com.project.gas.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

//	@Autowired
//	private UserService service;

	@GetMapping("/signup")
	public String sign_up() {
		return "/menus/sign_up";
	}

	@GetMapping("/signin")
	public String sign_in() {
		return "/menus/sign_in";
	}

	@GetMapping("/fav")
	public String fav() {
		return "/menus/fav";
	}

	@GetMapping("/findpw")
	public String findpw() {
		return "/menus/findpw";
	}

}
