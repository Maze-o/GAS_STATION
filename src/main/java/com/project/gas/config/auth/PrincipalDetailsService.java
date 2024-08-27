package com.project.gas.config.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.gas.user.User;
import com.project.gas.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {
	private final UserRepository userRepo;

	// 1. 패스워드는 알아서 체킹하니까 신경쓸 필요 없다.
	// 2. 리턴이 잘되면 자동으로 세션을 만든다.
	
	//loadbyusername으로 사용자 정보를 가져옴
	@Override
	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {

		Optional<User> userOptional = userRepo.findByUserid(userid);

		if (userOptional.isPresent()) {
			return new PrincipalDetails(userOptional.get());
		} else {
			return null;

		}
	}

//	@Override
//	public User loadUserByusername(String userid) {
//		return userRepo.findByUserid(userid)
//				.orElseThrow(() -> new IllegalArgumentException(userid));
//	}
}
