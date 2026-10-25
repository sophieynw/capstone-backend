package ca.sheridancollege.restfulhousekeeping.services;

import io.imagekit.client.ImageKitClient;
import io.imagekit.client.okhttp.ImageKitOkHttpClient;
import io.imagekit.models.files.FileDeleteParams;
import io.imagekit.models.files.FileUploadParams;
import io.imagekit.models.files.FileUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageKitService {

    private final String privateKey;

    public ImageKitService(@Value("${imagekit.private-key:}") String privateKey) {
        this.privateKey = privateKey;
    }

    public UploadedImage upload(MultipartFile file, String folder) throws IOException {
        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" +
                (originalName == null || originalName.isBlank() ? "image.jpg" : originalName);

        FileUploadParams params = FileUploadParams.builder()
                .file(file.getBytes())
                .fileName(fileName)
                .folder(folder)
                .build();

        FileUploadResponse response = client().files().upload(params);
        String url = response.url().orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "ImageKit upload did not return a URL"
        ));
        String fileId = response.fileId().orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "ImageKit upload did not return a file ID"
        ));

        return new UploadedImage(url, fileId);
    }

    public void delete(String fileId) {
        if (fileId == null || fileId.isBlank()) {
            return;
        }

        client().files().delete(FileDeleteParams.builder()
                .fileId(fileId)
                .build());
    }

    private ImageKitClient client() {
        if (privateKey == null || privateKey.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "ImageKit is not configured"
            );
        }

        return ImageKitOkHttpClient.builder()
                .privateKey(privateKey)
                .build();
    }

    public record UploadedImage(String url, String fileId) {
    }
}
