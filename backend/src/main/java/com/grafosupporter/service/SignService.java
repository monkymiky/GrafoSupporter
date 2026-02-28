package com.grafosupporter.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grafosupporter.model.Sign;
import com.grafosupporter.repository.SignRepository;

@Service
public class SignService {
    private final SignRepository signRepository;

    public SignService(SignRepository signRepository) {
        this.signRepository = signRepository;
    }

    @Transactional(readOnly = true)
    public List<Sign> getAllSign() {
        return signRepository.findAll();
    }

}
