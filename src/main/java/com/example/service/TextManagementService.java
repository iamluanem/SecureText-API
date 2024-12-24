package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.util.Base64;

import com.example.dto.DecryptionResponse;
import com.example.dto.EncryptionResponse;
import com.example.dto.TextRequest;
import com.example.entity.TextData;
import com.example.repository.TextDataRepository;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Service
@Slf4j
public class TextManagementService {

    @Autowired
    private TextDataRepository repository;
    
    @Autowired
    private EncryptionService encryptionService;

    public EncryptionResponse processText(TextRequest request) {
        String id = UUID.randomUUID().toString();
        
        if (request.isEncryption()) {
            if (request.getKeySize() == 0) {
                request.setKeySize(2048);
            }
            if (request.getKeySize() != 1024 && request.getKeySize() != 2048 && request.getKeySize() != 4096) {
                throw new IllegalArgumentException("Key size must be 1024, 2048, or 4096 bits");
            }
        }

        if (!request.isEncryption()) {
            TextData textData = TextData.builder()
                .id(id)
                .encryptedText(request.getTextData())
                .encrypted(false)
                .build();
                
            repository.save(textData);
            return new EncryptionResponse(id, null);
        }
        
        try {
            KeyPair keyPair = encryptionService.generateKeyPair(request.getKeySize());
            String encryptedText = encryptionService.encrypt(request.getTextData(), keyPair.getPublic());
            
            TextData textData = TextData.builder()
                .id(id)
                .encryptedText(encryptedText)
                .encrypted(true)
                .keySize(request.getKeySize())
                .publicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()))
                .build();
                
            repository.save(textData);
            
            String privateKey = encryptionService.formatPrivateKey(keyPair.getPrivate());
            return new EncryptionResponse(id, privateKey);
            
        } catch (Exception e) {
            log.error("Erro ao processar texto", e);
            throw new RuntimeException("Erro ao processar texto", e);
        }
    }

    public DecryptionResponse getText(String id, String privateKeyEncoded) {
        TextData textData = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Text ID not found"));
    
        if (!textData.isEncrypted()) {
            return new DecryptionResponse(textData.getEncryptedText());
        }
    
        if (privateKeyEncoded == null || privateKeyEncoded.isEmpty()) {
            throw new IllegalArgumentException("Private key is required for decryption");
        }
    
        try {
            // Decodificar a chave privada do formato Base64 e tratar caracteres escapados
            String privateKeyPem = new String(Base64.getDecoder().decode(privateKeyEncoded), StandardCharsets.UTF_8)
                .replace("\\n", "\n");  // Substituir \\n por \n
    
            // Converter a chave PEM para o formato utilizável
            PrivateKey privateKey = encryptionService.parsePrivateKey(privateKeyPem);
    
            // Descriptografar o texto
            String decryptedText = encryptionService.decrypt(textData.getEncryptedText(), privateKey);
            return new DecryptionResponse(decryptedText);
        } catch (Exception e) {
            log.error("Erro ao descriptografar texto", e);
            throw new RuntimeException("Erro ao descriptografar texto", e);
        }
    }
} 