package ca.sheridancollege.restfulhousekeeping.services;

import ca.sheridancollege.restfulhousekeeping.beans.User;
import ca.sheridancollege.restfulhousekeeping.models.*;
import ca.sheridancollege.restfulhousekeeping.repositories.ChecklistItemRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserResponseService {
    private final UserRepository userRepository;

    @Transactional
    public Optional<UserResponse> updateUser(Long id, UpdateUserRequest request) {

        return userRepository.findById(id).map(existing -> {

            if (request.getFirstName() != null) {
                existing.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
                existing.setLastName(request.getLastName());
            }
            if (request.getEmail() != null) {
                existing.setEmail(request.getEmail());
            }
            User saved = userRepository.save(existing);
            return toUserResponse(saved);
        });
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .username(user.getUsername())
                .role(user.getRole())
                .organization(user.getOrganization())
                .build();
    }

}