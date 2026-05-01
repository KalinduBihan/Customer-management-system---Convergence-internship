package com.example.demo.exception;

/**
 * Exception thrown when input validation fails
 */
public class InvalidInputException extends RuntimeException {
    
    private final String field;
    private final String reason;
    
    public InvalidInputException(String message) {
        super(message);
        this.field = null;
        this.reason = null;
    }
    
    public InvalidInputException(String message, String field, String reason) {
        super(message);
        this.field = field;
        this.reason = reason;
    }
    
    public String getField() {
        return field;
    }
    
    public String getReason() {
        return reason;
    }
}