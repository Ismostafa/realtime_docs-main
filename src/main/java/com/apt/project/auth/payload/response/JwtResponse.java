package com.apt.project.auth.payload.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class JwtResponse {
	private String token;
    private String id;
	private String username;
	private String email;

	public JwtResponse(String accessToken, String id, String username, String email) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
	}

}
