package com.example.exception;

public class TextManagementException extends RuntimeException {
    public TextManagementException(String message) {
        super(message);
    }

    public TextManagementException(String message, Throwable cause) {
        super(message, cause);
    }
} 