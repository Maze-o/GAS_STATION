package com.project.gas.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements UserDetails {
	private String userPk;
	private String userId;
	private String userPw;
	private String userName;
	private String regDt;
	private List<Roles> Roles;
	
	
	@Override 
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return Roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleKey()))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
}
