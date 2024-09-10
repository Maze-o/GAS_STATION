package com.project.gas.user;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.gas.dto.JoinRequest;
import com.project.gas.dto.LoginRequest;
import com.project.gas.dto.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepo;
	private final BCryptPasswordEncoder encoder;

	public boolean checkLoginIdDuplicate(String userid) {
		return userRepo.existsByuserid(userid);
	}

	// 회원가입 서비스 처리
	public void signup(JoinRequest joinRequest) {
		userRepo.save(joinRequest.toEntity());
	}

	// 로그인 서비스 처리
	public User signin(LoginRequest loginRequest) {

		User findUser = userRepo.findByuserid(loginRequest.getUserid());

		System.err.println("service : " + findUser);

		if (findUser == null) {
			System.out.println("아이디틀렸어!~!!!!!!");
			return null;
		}


		if (BCrypt.checkpw(loginRequest.getUserpw(), findUser.getUserpw())) {

			return findUser;
		}
		return null;
	}

	public User getLoginMemberById(Long userid) {
		if (userid == null)
			return null;

		Optional<User> findMember = userRepo.findById(userid);
		return findMember.orElse(null);

	}

	// 회원가입 비밀번호 암호화 처리
	public void securityJoin(JoinRequest joinRequest) {
		if (userRepo.existsByuserid(joinRequest.getUserid())) {
			return;
		}

		joinRequest.setUserpw(encoder.encode(joinRequest.getUserpw()));

		userRepo.save(joinRequest.toEntity());
	}

}
