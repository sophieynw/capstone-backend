package ca.sheridancollege.restfulhousekeeping.beans;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Person {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	//@NonNull
	private String name;
	private String email;
	// TEMPORARY, for testing. Passwords will eventually be stored NOT in a string.
	private String password;
	private String role;
	private boolean available;
	// Consider revising this if scope expands to support more time zones
	private LocalDateTime createdAt;
}