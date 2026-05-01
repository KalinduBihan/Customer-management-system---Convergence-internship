package com.example.demo.exception;

/**
 * Exception thrown when trying to create a customer with a duplicate NIC
 */
public class DuplicateNicException extends RuntimeException {
    
    private final String nic;
    
    public DuplicateNicException(String message) {
        super(message);
        this.nic = null;
    }
    
    public DuplicateNicException(String message, String nic) {
        super(message);
        this.nic = nic;
    }
    
    public String getNic() {
        return nic;
    }
}