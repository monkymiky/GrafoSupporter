package com.grafosupporter.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.grafosupporter.service.ImageFileService;

import jakarta.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URI;

import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/files")
public class ImageFileController {
    private final ImageFileService fileStorageService;
    private final ServletContext servletContext;

    public ImageFileController(ImageFileService fileStorageService, ServletContext servletContext) {
        this.fileStorageService = fileStorageService;
        this.servletContext = servletContext;
    }

    @GetMapping("/combination-image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename)
            throws MalformedURLException, FileNotFoundException {
        Resource resource = this.fileStorageService.getImageFile(filename);

        String contentType = servletContext.getMimeType(resource.getFilename());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .cacheControl(cacheControl)
                .body(resource);

    }

    @PostMapping("/combination-image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestPart("imageFile") MultipartFile imageFile)
            throws IOException {
        Map<String, String> response = new HashMap<>();
        final String MESSAGE = "message";
        if (imageFile.isEmpty()) {
            response.put(MESSAGE, "Nessun file selezionato.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String uniqueFilename = fileStorageService.addImageFile(imageFile);

        response.put(MESSAGE, "Immagine caricata con successo!");
        response.put("fileName", uniqueFilename);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files/combination-image/{filename}")
                .buildAndExpand(uniqueFilename)
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
}