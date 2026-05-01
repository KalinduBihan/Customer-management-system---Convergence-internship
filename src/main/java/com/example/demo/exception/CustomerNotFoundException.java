package com.example.demo.exception;

/**
 * Exception thrown when a customer is not found in the database
 */
public class CustomerNotFoundException extends RuntimeException {
    
    private final Long customerId;
    
    public CustomerNotFoundException(String message) {
        super(message);
        this.customerId = null;
    }
    
    public CustomerNotFoundException(String message, Long customerId) {
        super(message);
        this.customerId = customerId;
    }
    
    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.customerId = null;
    }
    
    public Long getCustomerId() {
        return customerId;
    }
}