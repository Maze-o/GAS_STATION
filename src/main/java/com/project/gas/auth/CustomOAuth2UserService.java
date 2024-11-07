package com.project.gas.auth;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.project.gas.dto.User;
import com.project.gas.user.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

//    @Lazy
	private final UserRepository userRepo;
	private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest); // OAuth2 사용자 정보 로드
		logger.info("OAuth2 User Info: " + oAuth2User.getAttributes());
		// 사용자 정보를 기반으로 DB에서 사용자 검색 또는 생성
		User user = processOAuth2User(oAuth2User); // 직접 처리 메서드 호출

		// PrincipalDetails 생성 및 반환
		return new PrincipalDetails(user, oAuth2User.getAttributes());

	}

	// OAuth2 사용자 처리
	private User processOAuth2User(OAuth2User oAuth2User) {
		Map<String, Object> attributes = oAuth2User.getAttributes();
		String email;
		String name;

		// 제공되는 정보에 따라 구분하여 email과 name을 추출
		if (attributes.containsKey("response")) {
			// 네이버 로그인
			Map<String, Object> response = (Map<String, Object>) attributes.get("response");
			email = (String) response.get("email");
			name = (String) response.get("name");
		} else {
			// 구글 로그인
			email = (String) attributes.get("email");
			name = (String) attributes.get("name");
		}
//		String email = (String) attributes.get("email");
//        String name = (String) attributes.get("name");

		// DB에서 사용자를 찾거나 없으면 새로 생성
		return userRepo.findByuserid(email).orElseGet(() -> {
			User newUser = new User();
			newUser.setUserid(email); // 이메일을 사용자 ID로 사용
			System.out.println("userid : " + newUser.getUserid());
			newUser.setUsername(name); // 사용자 이름 설정
			newUser.setUserpw(null); // 소셜 로그인인 경우 비밀번호는 null로 설정
			return userRepo.save(newUser);
		});
	}
}