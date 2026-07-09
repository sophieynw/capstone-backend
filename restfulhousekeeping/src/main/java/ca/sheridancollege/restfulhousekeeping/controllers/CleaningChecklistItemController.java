//package ca.sheridancollege.restfulhousekeeping.controllers;
//
//import java.util.List;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ca.sheridancollege.restfulhousekeeping.beans.CleaningChecklistItem;
//import ca.sheridancollege.restfulhousekeeping.repositories.CleaningChecklistItemRepository;
//
//@RestController
//@RequestMapping("/cleaning-checklist-items")
//public class CleaningChecklistItemController {
//
//    private final CleaningChecklistItemRepository cleaningChecklistItemRepository;
//
//    public CleaningChecklistItemController(CleaningChecklistItemRepository cleaningChecklistItemRepository) {
//        this.cleaningChecklistItemRepository = cleaningChecklistItemRepository;
//    }
//
//    @GetMapping
//    public List<CleaningChecklistItem> getAll() {
//        return cleaningChecklistItemRepository.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CleaningChecklistItem> getById(@PathVariable Long id) {
//        return cleaningChecklistItemRepository.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public CleaningChecklistItem create(@RequestBody CleaningChecklistItem item) {
//        return cleaningChecklistItemRepository.save(item);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<CleaningChecklistItem> update(@PathVariable Long id, @RequestBody CleaningChecklistItem updated) {
//        return cleaningChecklistItemRepository.findById(id).map(existing -> {
//            existing.setIsComplete(updated.getIsComplete());
//            return ResponseEntity.ok(cleaningChecklistItemRepository.save(existing));
//        }).orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        if (!cleaningChecklistItemRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        cleaningChecklistItemRepository.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//}