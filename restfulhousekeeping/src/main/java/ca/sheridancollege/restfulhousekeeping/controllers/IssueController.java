//package ca.sheridancollege.restfulhousekeeping.controllers;
//
//import java.util.List;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ca.sheridancollege.restfulhousekeeping.beans.Issue;
//import ca.sheridancollege.restfulhousekeeping.repositories.IssueRepository;
//
//@RestController
//@RequestMapping("/issues")
//public class IssueController {
//
//    private final IssueRepository issueRepository;
//
//    public IssueController(IssueRepository issueRepository) {
//        this.issueRepository = issueRepository;
//    }
//
//    @GetMapping
//    public List<Issue> getAll() {
//        return issueRepository.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Issue> getById(@PathVariable Long id) {
//        return issueRepository.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public Issue create(@RequestBody Issue issue) {
//        return issueRepository.save(issue);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Issue> update(@PathVariable Long id, @RequestBody Issue updated) {
//        return issueRepository.findById(id).map(existing -> {
//            existing.setDescription(updated.getDescription());
//            existing.setGuestName(updated.getGuestName());
//            return ResponseEntity.ok(issueRepository.save(existing));
//        }).orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        if (!issueRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        issueRepository.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//}