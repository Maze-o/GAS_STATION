package com.project.gas.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.gas.dto.User;
import com.project.gas.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {
	private final UserRepository userRepo;

	// 유저의 아이디를 받아서 DB와 대치시켜 리턴값을 반환
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 사용자 조회 로직
		User user = userRepo.findByuserid(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

		// User를 PrincipalDetails로 변환하여 반환
		return new PrincipalDetails(user);
	}	
}