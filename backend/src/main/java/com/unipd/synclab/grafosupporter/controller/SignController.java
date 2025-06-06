package com.unipd.synclab.grafosupporter.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unipd.synclab.grafosupporter.model.Sign;
import com.unipd.synclab.grafosupporter.service.SignService;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/signs")
@CrossOrigin(origins = "http://localhost:4200")
public class SignController {
    private final SignService signService;

    public SignController(SignService signService) {
        this.signService = signService;
    }

    @GetMapping
    public List<Sign> getMethodName() {
        return signService.getAllSign();
    }
}
