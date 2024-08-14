package com.project.gas.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public boolean booluser (UserDTO dto) {
		User user = userRepository.findByUserId(dto.getuserPk())
				.orElseThrow(() -> new UsernameNOtFoundException("ㄴㄴ" + dto.getId()));
		
		return user.getuserPw().equals(dto.getuserPw());
		
	}
}
