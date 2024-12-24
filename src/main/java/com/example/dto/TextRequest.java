package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextRequest {
    private String textData;
    private boolean encryption;
    @Builder.Default
    private int keySize = 2048;
    private String privateKeyPassword;

    @AssertTrue(message = "Key size must be 1024, 2048 or 4096")
    public boolean isValidKeySize() {
        return !encryption || (keySize == 1024 || keySize == 2048 || keySize == 4096);
    }
} 