package com.project.gas.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.gas.dto.User;

//로그인 구현 관련
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//	Optional <User> findByUserid(String userid); //user id로 정보를 가져온다.
	boolean existsByuserid(String userid);

	Optional<User> findByuserid(String userid);

}
