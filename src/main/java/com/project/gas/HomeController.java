package com.project.gas;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public static String goHome(HttpServletRequest request) {
		return "content/home";
	}
}
