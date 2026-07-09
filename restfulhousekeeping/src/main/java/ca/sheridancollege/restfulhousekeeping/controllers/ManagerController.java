package ca.sheridancollege.restfulhousekeeping.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.sheridancollege.restfulhousekeeping.beans.Manager;
import ca.sheridancollege.restfulhousekeeping.repositories.ManagerRepository;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerRepository managerRepository;

    public ManagerController(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @GetMapping
    public List<Manager> getAll() {
        return managerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manager> getById(@PathVariable Long id) {
        return managerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Manager create(@RequestBody Manager manager) {
        return managerRepository.save(manager);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manager> update(@PathVariable Long id, @RequestBody Manager updated) {
        return managerRepository.findById(id).map(existing -> {
            existing.setFirstName(updated.getFirstName());
            existing.setLastName(updated.getLastName());
            existing.setUserName(updated.getUserName());
            existing.setEmail(updated.getEmail());
            existing.setPasswordHash(updated.getPasswordHash());
            existing.setPhoneNumber(updated.getPhoneNumber());
            return ResponseEntity.ok(managerRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!managerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        managerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}