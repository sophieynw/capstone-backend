package ca.sheridancollege.restfulhousekeeping.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ca.sheridancollege.restfulhousekeeping.beans.Property;
import ca.sheridancollege.restfulhousekeeping.beans.Role;
import ca.sheridancollege.restfulhousekeeping.beans.User;
import ca.sheridancollege.restfulhousekeeping.models.PropertyResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.PropertyRepository;
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
    
    // CREATE new property record service
    @Transactional
    public PropertyResponse createProperty(Property property) {
    	property.setId(null);
    	
        if (property.getManager() == null || property.getManager().getId() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Manager ID is required"
            );
        }
    	
    	Long managerId = property.getManager().getId();
    	User manager = userRepository
    			.findById(managerId)
    			.orElseThrow(() -> new ResponseStatusException(
    					HttpStatus.NOT_FOUND,
    					"User not found: " + managerId));
    	if (manager.getRole() != Role.MANAGER) {
    		throw new ResponseStatusException(
    				HttpStatus.BAD_REQUEST,
    				"User " + managerId + " is not a manager");
    	}
    	
    	property.setManager(manager);
    	Property savedProperty = propertyRepository.save(property);
    	
    	return toPropertyResponse(savedProperty);
    			
    	
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
