package ca.sheridancollege.restfulhousekeeping.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.restfulhousekeeping.beans.Cleaning;
import ca.sheridancollege.restfulhousekeeping.models.CleaningResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningRepository;
import ca.sheridancollege.restfulhousekeeping.services.CleaningResponseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/cleanings")
@SecurityRequirement(name = "Bearer Authentication")
public class CleaningController {

	private final CleaningRepository cleaningRepository;
	private final CleaningResponseService cleaningResponseService;

	// Home Page API
	// GET all upcoming cleanings by userId
	@GetMapping("/upcoming/{userId}")
	public List<CleaningResponse> getMyUpcomingCleanings(@PathVariable Long userId) {
		return cleaningResponseService.getMyUpcomingCleanings(userId);
	}

	@GetMapping("/upcoming/{propertyId}/first")
	public CleaningResponse getNextCleaningByPropertyId(@PathVariable Long propertyId) {
		return cleaningResponseService.getFirstCleaningByProperty(propertyId);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cleaning> getById(@PathVariable Long id) {
		return cleaningRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Cleaning create(@RequestBody Cleaning cleaning) {
		return cleaningRepository.save(cleaning);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cleaning> update(@PathVariable Long id, @RequestBody Cleaning updated) {
		return cleaningRepository.findById(id).map(existing -> {
			existing.setDateTimeStart(updated.getDateTimeStart());
			existing.setDateTimeEnd(updated.getDateTimeEnd());
			existing.setNotes(updated.getNotes());
			existing.setIsComplete(updated.getIsComplete());
			return ResponseEntity.ok(cleaningRepository.save(existing));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if (!cleaningRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		cleaningRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}