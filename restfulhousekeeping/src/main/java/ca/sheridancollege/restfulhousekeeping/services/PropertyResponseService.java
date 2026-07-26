package ca.sheridancollege.restfulhousekeeping.services;

import java.time.LocalDateTime;
import java.util.List;

import ca.sheridancollege.restfulhousekeeping.beans.Property;
import ca.sheridancollege.restfulhousekeeping.models.PropertyResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.PropertyRepository;
import org.springframework.stereotype.Service;

import ca.sheridancollege.restfulhousekeeping.beans.Role;
import ca.sheridancollege.restfulhousekeeping.beans.User;
import ca.sheridancollege.restfulhousekeeping.models.CleaningResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PropertyResponseService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final CleaningChecklistItemResponseService cciResponseService;

    public List<PropertyResponse> getPropertyByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime now = LocalDateTime.now();
        List<Property> properties;

        if (user.getRole() == Role.MANAGER) {
            properties = propertyRepository
                    .findByManagerId(
                            user.getId(),
                            now
                    );
        } else {
            throw new RuntimeException("Invalid user role");
        }

        return properties.stream()
                .map(this::toPropertyResponse)
                .toList();

    }

    private PropertyResponse toPropertyResponse(Property property) {
        return PropertyResponse.builder()
                .id(property.getId())
                .managerId(property.getManager().getId())
                .name(property.getName())
                .street(property.getStreet())
                .unit(property.getUnit())
                .city(property.getCity())
                .province(property.getCity())
                .postalCode(property.getPostalCode())
                .country(property.getCountry())
                .accessInstructions(property.getCountry())

                .build();
    }

}
