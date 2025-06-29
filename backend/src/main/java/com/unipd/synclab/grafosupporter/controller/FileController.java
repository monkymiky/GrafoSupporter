package com.unipd.synclab.grafosupporter.controller;

import com.unipd.synclab.grafosupporter.config.UploadProperties;
import com.unipd.synclab.grafosupporter.service.FileStorageService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileStorageService fileStorageService;
    private final ServletContext servletContext;
    private final UploadProperties uploadProperties;

    public FileController(FileStorageService fileStorageService, ServletContext servletContext,
            UploadProperties uploadProperties) {
        this.fileStorageService = fileStorageService;
        this.servletContext = servletContext;
        this.uploadProperties = uploadProperties;
    }

    @GetMapping("/combination-image/{filename:.+}")
    public ResponseEntity<Resource> serveCombinationImage(@PathVariable String filename) {
        try {

            Path filePath = fileStorageService.getImagePath(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = servletContext.getMimeType(filePath.toAbsolutePath().toString());
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic();

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .cacheControl(cacheControl)
                        .body(resource);
            } else {
                throw new FileNotFoundException("File non trovato: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore interno nel recupero del file.",
                    e);
        } catch (FileNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File non trovato: " + filename, e);
        }
    }

    @PostMapping("/combination-image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestPart("imageFile") MultipartFile imageFile) {
        Map<String, String> response = new HashMap<>();

        if (imageFile.isEmpty()) {
            response.put("message", "Nessun file selezionato.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {

            if (imageFile.getContentType() != null) {
                if (!imageFile.getContentType().startsWith("image/")) {
                    response.put("message", "Solo file immagine sono consentiti.");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                if (imageFile.getSize() > uploadProperties.getMaxFileSize()) {
                    response.put("message", "Il file è troppo grande. Dimensione massima 5MB.");
                    return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
                }
                String filename = StringUtils.cleanPath(imageFile.getOriginalFilename());
                String extension = StringUtils.getFilenameExtension(filename);
                String newFilename = UUID.randomUUID().toString() + "." + extension;
                Path filePath = fileStorageService.getImagePath(newFilename);

                try (InputStream inputStream = imageFile.getInputStream()) {
                    Files.copy(inputStream, filePath);
                }

                response.put("message", "Immagine caricata con successo!");
                response.put("fileName", newFilename);
                response.put("filePath", filePath.toAbsolutePath().toString());
                return new ResponseEntity<>(response, HttpStatus.OK);

            } else {
                response.put("message", "Tentativo upload di un file vuoto o senza content type");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (

        IOException e) {
            response.put("message", "Errore durante il caricamento dell'immagine: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}