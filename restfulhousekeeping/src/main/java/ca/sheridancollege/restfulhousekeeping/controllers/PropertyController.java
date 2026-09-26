package ca.sheridancollege.restfulhousekeeping.controllers;

import java.util.List;

import ca.sheridancollege.restfulhousekeeping.models.UpdatePropertyRequest;import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.sheridancollege.restfulhousekeeping.beans.Property;
import ca.sheridancollege.restfulhousekeeping.models.CreatePropertyRequest;
import ca.sheridancollege.restfulhousekeeping.models.PropertyResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.PropertyRepository;
import ca.sheridancollege.restfulhousekeeping.services.PropertyResponseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/properties")
@SecurityRequirement(name = "Bearer Authentication")
public class PropertyController {

    private final PropertyRepository propertyRepository;
    private final PropertyResponseService propertyResponseService;

    @GetMapping
    public List<Property> getAll() {
        return propertyRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getById(@PathVariable Long id) {
        return propertyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get ALL properties by userId
    @GetMapping("/managers/{userId}")
    public List<PropertyResponse> getPropertyByUserId(@PathVariable Long userId) {
        return propertyResponseService.getPropertyByUserId(userId);
    }

    // CREATE new property record
    @PostMapping
    public ResponseEntity<PropertyResponse> create(
            @RequestBody CreatePropertyRequest request) {

        PropertyResponse response =
                propertyResponseService.createProperty(
                        request.getProperty(),
                        request.getChecklistItems()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PropertyResponse> update(@PathVariable Long id, @RequestBody UpdatePropertyRequest updated) {
        return propertyResponseService.updateProperty(id, updated)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        if (!propertyRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        propertyRepository.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return propertyRepository.findById(id)
                .map(property -> {
                    propertyRepository.delete(property);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}