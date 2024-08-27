package com.project.gas.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//로그인 구현 관련
public interface UserRepository extends JpaRepository<User, Long> {
	Optional <User> findByUserid(String userid); //user id로 정보를 가져온다.
}
