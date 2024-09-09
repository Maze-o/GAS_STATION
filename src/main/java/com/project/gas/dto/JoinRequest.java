package com.project.gas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class JoinRequest {

    private String userid;

    private String userpw;
    private String userpwchk;


    private String username;

    public User toEntity(){
        return User.builder()
                .username(this.username)
                .userpw(this.userpw)
                .userid(this.userid)
                .build();
    }
	
	
}
