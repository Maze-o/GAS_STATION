package com.project.gas.user;

<<<<<<< HEAD
import java.util.Optional;

=======
>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.gas.dto.User;

//로그인 구현 관련
public interface UserRepository extends JpaRepository<User, Long> {
//	Optional <User> findByUserid(String userid); //user id로 정보를 가져온다.
	
	boolean existsByuserid(String userid);
	
<<<<<<< HEAD
//	User findByuserid(String userid);
	Optional<User> findByuserid(String userid);
	
=======
	User findByuserid(String userid);
>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc


}
