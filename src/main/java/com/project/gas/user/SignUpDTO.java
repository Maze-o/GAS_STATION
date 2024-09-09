package com.project.gas.user;

import com.project.gas.dto.User;

import lombok.Data;

@Data
public class SignUpDTO {
	private String username;
	private String userpw;
	private String userid;
	
	
	public User toEntity() {
		return User.builder()
				.username(username)
				.userpw(userpw)
				.userid(userid)
				.build();
	}
	

}
