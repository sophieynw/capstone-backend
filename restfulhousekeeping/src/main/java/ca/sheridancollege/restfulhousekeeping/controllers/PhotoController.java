package ca.sheridancollege.restfulhousekeeping.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ca.sheridancollege.restfulhousekeeping.beans.Cleaning;
import ca.sheridancollege.restfulhousekeeping.beans.Photo;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.PhotoRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/photos")
@SecurityRequirement(name = "Bearer Authentication")
public class PhotoController {

    private final PhotoRepository photoRepository;
    private final CleaningRepository cleaningRepository;

    // where photos get saved on the server
    private static final String UPLOAD_DIR = "uploads/photos/";

    // get all photos for a cleaning
    @GetMapping("/cleaning/{cleaningId}")
    public List<Photo> getByCleaningId(@PathVariable Long cleaningId) {
        return photoRepository.findByCleaningId(cleaningId);
    }

    // upload a photo for a cleaning
    @PostMapping(value = "/cleaning/{cleaningId}", consumes = "multipart/form-data")
    public ResponseEntity<Photo> upload(
            @PathVariable Long cleaningId,
            @RequestParam("file") MultipartFile file) throws IOException {

        Cleaning cleaning = cleaningRepository.findById(cleaningId)
                .orElseThrow(() -> new RuntimeException("Cleaning not found"));

        // make the upload folder if it doesn't exist yet
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // give the file a unique name so we don't overwrite existing ones
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, file.getBytes());

        // save the photo record to the database
        Photo photo = Photo.builder()
                .cleaning(cleaning)
                .fileName(fileName)
                .filePath(filePath.toString())
                .fileType(file.getContentType())
                .build();

        return ResponseEntity.ok(photoRepository.save(photo));
    }

    // delete a photo by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!photoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        photoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}