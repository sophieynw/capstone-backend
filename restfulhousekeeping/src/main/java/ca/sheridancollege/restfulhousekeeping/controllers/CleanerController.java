package ca.sheridancollege.restfulhousekeeping.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.sheridancollege.restfulhousekeeping.beans.Cleaner;
import ca.sheridancollege.restfulhousekeeping.repositories.CleanerRepository;

@RestController
@RequestMapping("/cleaners")
public class CleanerController {

    private final CleanerRepository cleanerRepository;

    public CleanerController(CleanerRepository cleanerRepository) {
        this.cleanerRepository = cleanerRepository;
    }

    @GetMapping
    public List<Cleaner> getAll() {
        return cleanerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cleaner> getById(@PathVariable Long id) {
        return cleanerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cleaner create(@RequestBody Cleaner cleaner) {
        return cleanerRepository.save(cleaner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cleaner> update(@PathVariable Long id, @RequestBody Cleaner updated) {
        return cleanerRepository.findById(id).map(existing -> {
            existing.setFirstName(updated.getFirstName());
            existing.setLastName(updated.getLastName());
            existing.setUserName(updated.getUserName());
            existing.setEmail(updated.getEmail());
            existing.setPasswordHash(updated.getPasswordHash());
            existing.setPhoneNumber(updated.getPhoneNumber());
            return ResponseEntity.ok(cleanerRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!cleanerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cleanerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}