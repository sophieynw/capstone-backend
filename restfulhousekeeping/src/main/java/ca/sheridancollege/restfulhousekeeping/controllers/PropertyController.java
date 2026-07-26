package ca.sheridancollege.restfulhousekeeping.controllers;

import java.util.List;

import ca.sheridancollege.restfulhousekeeping.models.PropertyResponse;
import ca.sheridancollege.restfulhousekeeping.services.PropertyResponseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.sheridancollege.restfulhousekeeping.beans.Property;
import ca.sheridancollege.restfulhousekeeping.repositories.PropertyRepository;

@RestController
@AllArgsConstructor
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyRepository propertyRepository;
    private final PropertyResponseService propertyResponseService;

//    public PropertyController(PropertyRepository propertyRepository) {
//        this.propertyRepository = propertyRepository;
//    }

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

    @PostMapping
    public Property create(@RequestBody Property property) {
        return propertyRepository.save(property);
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