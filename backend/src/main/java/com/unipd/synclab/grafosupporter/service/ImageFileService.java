package com.unipd.synclab.grafosupporter.service;

import com.unipd.synclab.grafosupporter.config.ConfigurationPropertiesManager;
import com.unipd.synclab.grafosupporter.exception.FileValidationException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageFileService {

    private final Path combinationUploadDir;
    private final Long maxFileSize;

    public ImageFileService(ConfigurationPropertiesManager configProperties) {
        this.combinationUploadDir = Paths.get(configProperties.getCombinationImagesDirectory())
                .toAbsolutePath()
                .normalize();
        this.maxFileSize = configProperties.getMaxFileSize();
        try {
            if (!Files.exists(this.combinationUploadDir)) {
                Files.createDirectories(this.combinationUploadDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Impossibile inizializzare la directory di upload: " + this.combinationUploadDir,
                    e);
        }

    }

    private Path getNormalizedFilePath(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del file non può essere nullo o vuoto.");
        }
        String normalizedFilename = StringUtils.cleanPath(filename);
        Path resolvedPath = combinationUploadDir.resolve(normalizedFilename).normalize();
        if (!resolvedPath.startsWith(combinationUploadDir)) {
            throw new IllegalArgumentException("Tentativo di path traversal rilevato: " + filename);
        }
        return resolvedPath;
    }

    public String addImageFile(MultipartFile file) throws IOException, FileValidationException {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new FileValidationException("Solo file immagine sono consentiti.");
        }
        if (file.getSize() > maxFileSize) {
            throw new FileValidationException("Il file è troppo grande. Dimensione massima 5MB.");
        }
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + "." + extension;
        Path filePath = this.getNormalizedFilePath(uniqueFileName);
        InputStream inputStream = file.getInputStream();
        Files.copy(inputStream, filePath);
        return uniqueFileName;
    }

    public Resource getImageFile(String filename) throws FileNotFoundException, MalformedURLException {
        Path filePath = this.getNormalizedFilePath(filename);
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException("File non trovato: " + filePath.getFileName().toString());
        }
    }

    public Boolean deleteImageFile(String fileName) throws IOException {
        Path fileToDelete = this.getNormalizedFilePath(fileName);
        if (!Files.exists(fileToDelete)) {
            System.out.println("Il file non esiste quindi non può essere eliminato. file = " + fileToDelete.toString());
            return false;
        }
        if (Files.isRegularFile(fileToDelete)) {
            Files.delete(fileToDelete);
            return true;
        } else {
            throw new FileNotFoundException("File non trovato o non è un file regolare, impossibile cancellare: "
                    + fileToDelete.toString());
        }

    }
}