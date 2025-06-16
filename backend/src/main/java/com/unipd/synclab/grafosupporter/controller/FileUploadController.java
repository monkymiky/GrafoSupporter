package com.unipd.synclab.grafosupporter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.unipd.synclab.grafosupporter.service.FileStorageService;

@RestController
@RequestMapping("/file-upload")
@CrossOrigin(origins = "http://localhost:4200")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
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
                if (imageFile.getSize() > 5 * 1024 * 1024) {
                    response.put("message", "Il file è troppo grande. Dimensione massima 5MB.");
                    return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
                }
                String filename = StringUtils.cleanPath(imageFile.getOriginalFilename());
                String extension = StringUtils.getFilenameExtension(filename);
                String newFilename = UUID.randomUUID().toString() + "." + extension;
                Path filePath = fileStorageService.getImagePath(newFilename);

                System.out.println("DEBUG: Tentativo di salvare il file a: " + filePath.toAbsolutePath().toString());

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
