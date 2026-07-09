package ca.sheridancollege.restfulhousekeeping.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.sheridancollege.restfulhousekeeping.beans.AvailabilitySlot;
import ca.sheridancollege.restfulhousekeeping.repositories.SlotRepository;

@RestController
@RequestMapping("/availability-slots")
public class AvailabilitySlotController {

    private final SlotRepository slotRepository;

    public AvailabilitySlotController(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @GetMapping
    public List<AvailabilitySlot> getAll() {
        return slotRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilitySlot> getById(@PathVariable Long id) {
        return slotRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AvailabilitySlot create(@RequestBody AvailabilitySlot slot) {
        return slotRepository.save(slot);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvailabilitySlot> update(@PathVariable Long id, @RequestBody AvailabilitySlot updated) {
        return slotRepository.findById(id).map(existing -> {
            existing.setDayOfWeek(updated.getDayOfWeek());
            existing.setStartTime(updated.getStartTime());
            existing.setEndTime(updated.getEndTime());
            return ResponseEntity.ok(slotRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!slotRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        slotRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}