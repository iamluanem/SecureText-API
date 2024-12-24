package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.DecryptionResponse;
import com.example.dto.EncryptionResponse;
import com.example.dto.TextRequest;
import com.example.service.TextManagementService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/text-management")
@Slf4j
public class TextManagementController {

    @Autowired
    private TextManagementService textManagementService;

    @PostMapping
    public ResponseEntity<EncryptionResponse> encryptText(@RequestBody TextRequest request) {
        log.info("Recebendo requisição para processar texto");
        
        EncryptionResponse response = textManagementService.processText(request);
        
        log.info("Texto processado com sucesso. ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/decrypt")
    public ResponseEntity<DecryptionResponse> getText(
            @RequestParam String id,
            @RequestParam(name = "privateKey") String privateKeyPem) {
        
        return ResponseEntity.ok(textManagementService.getText(id, privateKeyPem));
    }
} 
