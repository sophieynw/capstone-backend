package ca.sheridancollege.restfulhousekeeping.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import ca.sheridancollege.restfulhousekeeping.models.UpdateUserRequest;
import ca.sheridancollege.restfulhousekeeping.services.UserResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    
    // upload a profile picture for a user
    @PostMapping(value = "/{id}/profile-picture", consumes = "multipart/form-data")
    public ResponseEntity<UserResponse> uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // make the upload folder if it doesn't exist yet
        File uploadDir = new File("uploads/profile-pictures/");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // give the file a unique name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get("uploads/profile-pictures/" + fileName);
        Files.write(filePath, file.getBytes());

        user.setProfilePicturePath(filePath.toString());
        userRepository.save(user);

        return ResponseEntity.ok(UserResponse.fromUser(user));
    }

    // get profile picture path for a user
    @GetMapping("/{id}/profile-picture")
    public ResponseEntity<String> getProfilePicture(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user.getProfilePicturePath());
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