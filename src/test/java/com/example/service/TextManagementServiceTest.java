package com.example.service;

import com.example.dto.TextRequest;
import com.example.dto.EncryptionResponse;
import com.example.repository.TextDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TextManagementServiceTest {

    @Mock
    private TextDataRepository repository;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private TextManagementService textManagementService;

    @Test
    void whenProcessTextWithoutEncryption_thenSaveAndReturnId() {
        // Given
        TextRequest request = TextRequest.builder()
            .textData("test data")
            .encryption(false)
            .build();

        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        EncryptionResponse response = textManagementService.processText(request);

        // Then
        assertNotNull(response.getId());
        assertNull(response.getPrivateKey());
        verify(repository).save(any());
    }
} 