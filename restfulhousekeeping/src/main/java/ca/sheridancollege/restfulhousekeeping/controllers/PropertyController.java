package ca.sheridancollege.restfulhousekeeping.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.restfulhousekeeping.beans.Property;
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
    public ResponseEntity<PropertyResponse> create(@RequestBody Property property) {
    	PropertyResponse response = propertyResponseService.createProperty(property);
    	return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> update(@PathVariable Long id, @RequestBody Property updated) {
        return propertyRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setStreet(updated.getStreet());
            existing.setUnit(updated.getUnit());
            existing.setCity(updated.getCity());
            existing.setProvince(updated.getProvince());
            existing.setPostalCode(updated.getPostalCode());
            existing.setCountry(updated.getCountry());
            existing.setAccessInstructions(updated.getAccessInstructions());
            return ResponseEntity.ok(propertyRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!propertyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        propertyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}