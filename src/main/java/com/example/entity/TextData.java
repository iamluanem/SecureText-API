package com.example.entity;

import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TextData {
    @Id
    private String id;
    
    @Column(length = 10000)
    private String encryptedText;
    
    private boolean encrypted;
    private int keySize;
    
    @Column(length = 4000)
    private String publicKey;
} 