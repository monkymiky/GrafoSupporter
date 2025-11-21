package com.grafosupporter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.grafosupporter.model.Sign;
import com.grafosupporter.service.SignService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/signs")
public class SignController {
    private final SignService signService;

    public SignController(SignService signService) {
        this.signService = signService;
    }

    @GetMapping
    public List<Sign> getAllSign() {
        return signService.getAllSign();
    }
}
