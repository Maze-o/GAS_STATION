package com.project.gas.config.auth;

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

<<<<<<< HEAD
	// 유저의 아이디를 받아서 DB와 대치시켜 리턴값을 반환
//	@Override
//	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
//
//		User user = userRepo.findByuserid(userid);
//		if (user != null) {
//			return new PrincipalDetails(user);
//		}
//		return null;
//	}
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// 사용자 조회 로직
//		return userRepo.findByuserid(username)
//				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 사용자 조회 로직
		User user = userRepo.findByuserid(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

		// User를 PrincipalDetails로 변환하여 반환
		return new PrincipalDetails(user);
	}
=======

	// 유저의 아이디를 받아서 DB와 대치시켜 리턴값을 반환 
	@Override
	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {

		User user = userRepo.findByuserid(userid);
		if (user != null) {
			return new PrincipalDetails(user);
		}
		return null;
	}
	
	
>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc
}
