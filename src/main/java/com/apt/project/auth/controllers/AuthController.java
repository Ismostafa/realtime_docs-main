package com.apt.project.auth.controllers;

import com.apt.project.auth.models.User;
import com.apt.project.auth.payload.request.LoginRequest;
import com.apt.project.auth.payload.request.SignupRequest;
import com.apt.project.auth.payload.response.JwtResponse;
import com.apt.project.auth.payload.response.MessageResponse;
import com.apt.project.auth.repository.UserRepository;
import com.apt.project.auth.security.jwt.JwtUtils;
import com.apt.project.auth.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	final
	AuthenticationManager authenticationManager;

	final
	UserRepository userRepository;

	final
	PasswordEncoder encoder;

	final
	JwtUtils jwtUtils;

	public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
	}

	@PostMapping("/sign-in")
	public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			JwtResponse jwtResponse = authenticateUser(loginRequest.username(), loginRequest.password());
			return ResponseEntity.ok(jwtResponse);
		} catch (BadCredentialsException e) {
			return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
		}
	}


	@PostMapping("/sign-up")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		try {
			if (userRepository.existsByUsername(signUpRequest.username())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Username is already taken!"));
			}


			// Create new user's account
			User newUser = new User(signUpRequest.username(),
					signUpRequest.email(),
					encoder.encode(signUpRequest.password()));

			userRepository.save(newUser);

			JwtResponse jwtResponse = authenticateUser(signUpRequest.username(), signUpRequest.password());
			return ResponseEntity.ok(jwtResponse);
		} catch (Exception e){
			return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
		}

	}

	public JwtResponse authenticateUser(String username, String password) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail());

	}
}
