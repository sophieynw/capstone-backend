package ca.sheridancollege.restfulhousekeeping.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.sheridancollege.restfulhousekeeping.beans.Cleaning;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningRepository;

@RestController
@RequestMapping("/cleanings")
public class CleaningController {

    private final CleaningRepository cleaningRepository;

    public CleaningController(CleaningRepository cleaningRepository) {
        this.cleaningRepository = cleaningRepository;
    }

    @GetMapping
    public List<Cleaning> getAll() {
        return cleaningRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cleaning> getById(@PathVariable Long id) {
        return cleaningRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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