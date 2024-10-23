package com.project.gas.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.gas.dto.User;

//로그인 구현 관련
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	// userid로 존재 여부 확인
	boolean existsByuserid(String userid);
	// username으로 존재 여부 확인
	boolean existsByUsername(String username);
	// userid로 정보 가져오기
	Optional<User> findByuserid(String userid);
	// userid와 nickname으로 정보 가져옴
	Optional<User> findByUseridAndUsername(String userid, String username);
}
