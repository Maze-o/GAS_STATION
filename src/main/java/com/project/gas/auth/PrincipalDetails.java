package com.project.gas.auth;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.project.gas.dto.User;

import lombok.Builder;
import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

	private static final long serialVersionUID = 1L;
	private User user;
	private String token;
	private Map<String, Object> attributes;

	@Builder
	public PrincipalDetails(User user) {
		this.user = user;
	}

	// OAuth2 로그인용 생성자 (JWT 토큰 없이)
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(); // 권한을 사용하지 않으므로 빈 리스트 반환
	}

	@Override
	public String getUsername() {
		return user.getUserid();
	}

	@Override
	public String getPassword() {
		return user.getUserpw();
	}

	// OAuth2
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return user.getUsername();
	}

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

	@Override
	public boolean isEnabled() {
		return true; // true = 사용 가능
	}
}