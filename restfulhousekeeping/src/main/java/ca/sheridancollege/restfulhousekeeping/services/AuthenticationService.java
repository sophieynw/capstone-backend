package ca.sheridancollege.restfulhousekeeping.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ca.sheridancollege.restfulhousekeeping.beans.User;
import ca.sheridancollege.restfulhousekeeping.models.AuthenticationRequest;
import ca.sheridancollege.restfulhousekeeping.models.AuthenticationResponse;
import ca.sheridancollege.restfulhousekeeping.models.RegisterRequest;
import ca.sheridancollege.restfulhousekeeping.models.UserDto;
import ca.sheridancollege.restfulhousekeeping.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private JwtService jwtService;
	private AuthenticationManager authenticationManager;
	
	// a method to register a new user in our DB and generate a JWT for them
	@SuppressWarnings("unchecked")
	public AuthenticationResponse register(RegisterRequest request) {
		User user = User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.username(request.getUsername())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.phoneNumber(request.getPhoneNumber())
				.role(request.getRole())
				.build();
		userRepository.save(user);
		var jwtToken = jwtService.generateToken(user);
		UserDto userDto = UserDto.builder()
		        .id(user.getId())
		        .organization(user.getOrganization())
		        .firstName(user.getFirstName())
		        .lastName(user.getLastName())
		        .username(user.getUsername())
		        .email(user.getEmail())
		        .phoneNumber(user.getPhoneNumber())
		        .role(user.getRole())
		        .build();
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.user(userDto)
				.build();
	}
	
	// a method to authenticate an existing user and generate a JWT for them
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				request.getUsername(), request.getPassword()));
		User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		UserDto userDto = UserDto.builder()
		        .id(user.getId())
		        .organization(user.getOrganization())
		        .firstName(user.getFirstName())
		        .lastName(user.getLastName())
		        .username(user.getUsername())
		        .email(user.getEmail())
		        .phoneNumber(user.getPhoneNumber())
		        .role(user.getRole())
		        .build();
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.user(userDto)
				.build();
	}

}
