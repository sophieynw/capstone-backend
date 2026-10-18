package ca.sheridancollege.restfulhousekeeping.controllers;

import ca.sheridancollege.restfulhousekeeping.beans.User;
import ca.sheridancollege.restfulhousekeeping.models.CreatePropertyRequest;
import ca.sheridancollege.restfulhousekeeping.models.PropertyResponse;
import ca.sheridancollege.restfulhousekeeping.models.UpdatePropertyRequest;
import ca.sheridancollege.restfulhousekeeping.repositories.PropertyRepository;
import ca.sheridancollege.restfulhousekeeping.services.PropertyResponseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/properties")
@SecurityRequirement(name = "Bearer Authentication")
public class PropertyController {

    private final PropertyRepository propertyRepository;
    private final PropertyResponseService propertyResponseService;

    // GET ALL properties for authenticated manager
    @GetMapping
    public ResponseEntity<List<PropertyResponse>> getAll(@AuthenticationPrincipal User manager) {
        List<PropertyResponse> properties = propertyResponseService.getPropertiesForManager(manager.getId());
        return ResponseEntity.ok(properties);
    }

    // GET property by propertyId, scoped to manager's own properties
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getById(@PathVariable Long id, @AuthenticationPrincipal User manager) {
        return propertyResponseService.getPropertyById(id, manager.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
