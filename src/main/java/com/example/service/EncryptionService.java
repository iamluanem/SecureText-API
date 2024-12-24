package com.example.service;

import org.springframework.stereotype.Service; 

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;

@Service
@Slf4j
public class EncryptionService {

    public KeyPair generateKeyPair(int keySize) throws Exception {
        if (keySize != 1024 && keySize != 2048 && keySize != 4096) {
            throw new IllegalArgumentException("Key size must be 1024, 2048, or 4096 bits");
        }
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        // Configurando explicitamente o public exponent (65537 é o padrão recomendado)
        BigInteger publicExponent = BigInteger.valueOf(65537);
        SecureRandom secureRandom = new SecureRandom();
        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keySize, publicExponent);

        // Inicializando o gerador de chaves com os parâmetros
        keyPairGenerator.initialize(spec, secureRandom);

        return keyPairGenerator.generateKeyPair();
    }

    public String encrypt(String text, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    public String formatPrivateKey(PrivateKey privateKey) {
        String base64Key = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        return "-----BEGIN PRIVATE KEY-----\n" +
                base64Key +
                "\n-----END PRIVATE KEY-----";
    }

    public PrivateKey parsePrivateKey(String privateKeyPem) throws Exception {
        // Remove cabeçalho, rodapé, espaços em branco e quebras de linha
        String privateKeyContent = privateKeyPem
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("[\\r\\n\\s]", "");

        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}

