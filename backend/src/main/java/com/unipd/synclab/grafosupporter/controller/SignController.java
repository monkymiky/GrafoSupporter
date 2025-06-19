package com.unipd.synclab.grafosupporter.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unipd.synclab.grafosupporter.model.Sign;
import com.unipd.synclab.grafosupporter.service.SignService;

@RestController
@RequestMapping("/signs")
public class SignController {
    @Autowired
    private SignService signService;

    @GetMapping
    public List<Sign> getAllSign() {
        return signService.getAllSign();
    }
}
