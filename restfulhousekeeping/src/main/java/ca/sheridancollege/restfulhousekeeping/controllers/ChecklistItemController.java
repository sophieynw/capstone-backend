//package ca.sheridancollege.restfulhousekeeping.controllers;
//
//import java.util.List;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ca.sheridancollege.restfulhousekeeping.beans.ChecklistItem;
//import ca.sheridancollege.restfulhousekeeping.repositories.ChecklistItemRepository;
//
//@RestController
//@RequestMapping("/checklist-items")
//public class ChecklistItemController {
//
//    private final ChecklistItemRepository checklistItemRepository;
//
//    public ChecklistItemController(ChecklistItemRepository checklistItemRepository) {
//        this.checklistItemRepository = checklistItemRepository;
//    }
//
//    @GetMapping
//    public List<ChecklistItem> getAll() {
//        return checklistItemRepository.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ChecklistItem> getById(@PathVariable Long id) {
//        return checklistItemRepository.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public ChecklistItem create(@RequestBody ChecklistItem checklistItem) {
//        return checklistItemRepository.save(checklistItem);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ChecklistItem> update(@PathVariable Long id, @RequestBody ChecklistItem updated) {
//        return checklistItemRepository.findById(id).map(existing -> {
//            existing.setDescription(updated.getDescription());
//            existing.setFrequencyDays(updated.getFrequencyDays());
//            return ResponseEntity.ok(checklistItemRepository.save(existing));
//        }).orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        if (!checklistItemRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        checklistItemRepository.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//}