package com.project.gas.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.gas.dto.JoinRequest;
import com.project.gas.dto.LoginRequest;
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
		System.out.println("userValue : " + user.getUserid());

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
	public String singup(JoinRequest joinRequest) {

		userService.securityJoin(joinRequest);

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
