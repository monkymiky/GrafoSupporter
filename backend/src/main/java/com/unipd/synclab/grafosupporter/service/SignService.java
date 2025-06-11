package com.unipd.synclab.grafosupporter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.unipd.synclab.grafosupporter.model.Sign;
import com.unipd.synclab.grafosupporter.repository.SignRepository;

import org.springframework.transaction.annotation.Transactional;

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
