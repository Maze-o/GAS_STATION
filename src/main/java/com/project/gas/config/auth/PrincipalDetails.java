package com.project.gas.config.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.gas.dto.User;

import lombok.Builder;
import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails {

<<<<<<< HEAD
//	private static final long serialVersionUID = 1L;
=======
	private static final long serialVersionUID = 1L;
>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc

	private User user;

	@Builder
	public PrincipalDetails(User user) {
		this.user = user;
	}


	@Override // 권한 반환 (필요할까..?)
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("user"));
	}

	// 사용자의 ID 반환
	@Override
	public String getUsername() {
		return user.getUserid();
	}
<<<<<<< HEAD
	
=======

>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc
	// 사용자의 PW반환
	@Override
	public String getPassword() {
		return user.getUserpw();
	}

<<<<<<< HEAD
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
=======
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc

	// 계정 사용 가능 여부 반환
	@Override
	public boolean isEnabled() {
		return true; // true = 사용가능
	}

<<<<<<< HEAD


=======
>>>>>>> 688f1d2f54dd7919aa84c038bb52fd8b528fd4cc
}
