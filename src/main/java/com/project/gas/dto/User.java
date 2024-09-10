package com.project.gas.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor // 기본 생성자 자동 추가
@AllArgsConstructor
@Entity // entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userpk;
	
	@Column(unique = true)
	private String userid;

	private String userpw;

	private String username;


	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate createDate; // 날짜

	@PrePersist // DB에 INSERT 되기 직전에 실행. 즉 DB에 값을 넣으면 자동으로 실행됨
	public void createDate() {
		this.createDate = LocalDate.now();
	}

}

//@Getter	
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Entity // DB에 테이블 자동 생성
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @Column(unique = true) // username 중목 안됨
//    private String username;
//    private String password;
//    private String name;
//    private String email;
//
//    private String role; // 권한
//
//    @DateTimeFormat(pattern = "yyyy-mm-dd")
//    private LocalDate createDate; // 날짜
//
//    @PrePersist // DB에 INSERT 되기 직전에 실행. 즉 DB에 값을 넣으면 자동으로 실행됨
//    public void createDate() {
//        this.createDate = LocalDate.now();
//    }
