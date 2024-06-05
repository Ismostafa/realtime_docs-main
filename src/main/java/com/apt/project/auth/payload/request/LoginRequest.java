package com.apt.project.auth.payload.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
		@NotBlank
		String username,

		@NotBlank
		String password
){}
