package com.project.gas;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
	@GetMapping("/")
	public static String goHome(HttpServletRequest request) {
		return "content/home";
	}
}
