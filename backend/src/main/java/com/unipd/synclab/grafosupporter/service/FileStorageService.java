// backend/src/main/java/com/unipd/synclab/grafosupporter/service/FileStorageService.java
package com.unipd.synclab.grafosupporter.service;

import com.unipd.synclab.grafosupporter.config.UploadProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final Path combinationUploadDir;
    private final UploadProperties uploadProperties;

    public FileStorageService(UploadProperties uploadProperties) throws IOException {
        this.uploadProperties = uploadProperties;
        this.combinationUploadDir = Paths.get(uploadProperties.getCombinationImagesDirectory()).toAbsolutePath();
        if (!Files.exists(this.combinationUploadDir)) {
            Files.createDirectories(this.combinationUploadDir);
        }
    }

    public Path getImagePath(String fileName) {
        return this.combinationUploadDir.resolve(fileName);
    }

    public boolean deleteFile(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        Path fileToDelete = getImagePath(fileName);
        try {
            if (Files.exists(fileToDelete) && Files.isRegularFile(fileToDelete)) {
                Files.delete(fileToDelete);
                return true;
            } else {
                System.out.println("DEBUG: File non trovato o non è un file regolare, impossibile cancellare: "
                        + fileToDelete.toString());
                return false;
            }
        } catch (IOException e) {
            System.err.println(
                    "ERRORE: Impossibile cancellare il file " + fileToDelete.toString() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}