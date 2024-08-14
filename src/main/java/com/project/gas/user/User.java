package com.project.gas.user;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter //게터 자동 생성
@NoArgsConstructor //기본 생성자 자동 추가 ( setter )
@Entity //entity
@Table(name = "user") //table name
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_Pk")
	private int userPk;
	
	@Column(name = "user_Id")
	private String userId;
	
	@Column(name = "user_Pw")
	private String userPw;
	
	@Column(name = "user_Name")
	private String userName;
	
	@Column(name = "regDt")
	private int regDt;

	/*
	 * @Builder public User(String userId, String userPw) { this.userId = userId;
	 * this.userPw = userPw; }
	 */
	
	public User encodePassword (PasswordEncoder passwordEncoder) {
		this.userPw = passwordEncoder.encode(this.userPw);
		return this;
	}
	
}
