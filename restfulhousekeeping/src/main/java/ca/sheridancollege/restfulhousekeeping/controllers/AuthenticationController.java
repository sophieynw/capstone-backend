package ca.sheridancollege.restfulhousekeeping.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.restfulhousekeeping.models.AuthenticationRequest;
import ca.sheridancollege.restfulhousekeeping.models.AuthenticationResponse;
import ca.sheridancollege.restfulhousekeeping.models.RegisterRequest;
import ca.sheridancollege.restfulhousekeeping.repositories.UserRepository;
import ca.sheridancollege.restfulhousekeeping.services.AuthenticationResponseService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	
	private final AuthenticationResponseService authenticationResponseService;
	private final UserRepository userRepository;
	
	// checks if username is available
	@GetMapping("/username-availability")
	public Map<String, Boolean> usernameAvailable(@RequestParam String username) {
		boolean available = !userRepository.existsByUsername(username.trim());
		return Map.of("available", available);
	}
	
	// map incoming POST requests to register a new user
	@PostMapping(value = "/register", consumes = "application/json")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(authenticationResponseService.register(request));
	}
	
	// map incoming POST requests to authenticate an existing user
	@PostMapping(value = "/authenticate", consumes = "application/json")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(authenticationResponseService.authenticate(request));
	}

}