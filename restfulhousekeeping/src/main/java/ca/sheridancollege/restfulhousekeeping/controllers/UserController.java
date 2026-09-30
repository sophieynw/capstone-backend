package ca.sheridancollege.restfulhousekeeping.controllers;

import java.util.List;

import ca.sheridancollege.restfulhousekeeping.models.UpdateUserRequest;
import ca.sheridancollege.restfulhousekeeping.services.UserResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.sheridancollege.restfulhousekeeping.beans.Role;
import ca.sheridancollege.restfulhousekeeping.beans.User;
import ca.sheridancollege.restfulhousekeeping.models.UserResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/cleaners")
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserRepository userRepository;
    private final UserResponseService userResponseService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{organizationId}/cleaners")
    public ResponseEntity<List<UserResponse>> getCleaners(@PathVariable Long organizationId) {
    	List<UserResponse> cleaners = userRepository
    			.findAllByOrganization_IdAndRole(organizationId, Role.CLEANER)
    			.stream().map(UserResponse::fromUser).toList();
    	return ResponseEntity.ok(cleaners);
    }

    @GetMapping("/available")
    public ResponseEntity<List<UserResponse>> getAvailableCleaners() {
    	List<UserResponse> cleaners = userRepository
    			.findAllByRoleAndOrganizationIsNull(Role.CLEANER)
    			.stream().map(UserResponse::fromUser).toList();
    	return ResponseEntity.ok(cleaners);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User updated) {
//        return userRepository.findById(id).map(existing -> {
//            existing.setFirstName(updated.getFirstName());
//            existing.setLastName(updated.getLastName());
//            existing.setUsername(updated.getUsername());
//            existing.setEmail(updated.getEmail());
//            existing.setPassword(updated.getPassword());
//            existing.setPhoneNumber(updated.getPhoneNumber());
//            return ResponseEntity.ok(userRepository.save(existing));
//        }).orElse(ResponseEntity.notFound().build());
//    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UpdateUserRequest updated) {
        return userResponseService.updateUser(id, updated)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}