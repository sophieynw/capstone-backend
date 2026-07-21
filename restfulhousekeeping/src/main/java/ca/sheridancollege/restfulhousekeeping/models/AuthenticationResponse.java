package ca.sheridancollege.restfulhousekeeping.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
	
	private String token;
	private String organizationName;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String phoneNumber;
	private String role;

}
