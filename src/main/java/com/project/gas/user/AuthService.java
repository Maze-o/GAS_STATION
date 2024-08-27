package com.project.gas.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {
	private final UserRepository userRepo;
	private final BCryptPasswordEncoder encoder;
	
	//회원가입 비밀번호 암호화
	@Transactional // Write(Insert, Update, Delete)
	public User signup(User user) {
		String rawPw = user.getUserpw();
		String encPw = encoder.encode(rawPw);
		user.setUserpw(encPw);
		
		User userEntity = userRepo.save(user);
		return userEntity;
		
	}
	

}
