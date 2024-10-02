package com.project.gas.user;

import java.util.Collections;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PostMapping;

import com.project.gas.dto.JoinRequest;
import com.project.gas.dto.LoginRequest;
import com.project.gas.jwt.JwtUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/signin")
    public String signin(Model model) {
        // 현재 사용자의 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

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
        return "menus/find_pw";
    }
}
