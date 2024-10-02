package com.project.gas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// 로그인 시 아이디와 패스워드를 받아올 객채
public class LoginRequest {

	private String userid;
    private String userpw;
    private String username;
}
