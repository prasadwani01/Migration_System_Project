package com.sample_project.migration_project.exception;

// We extend RuntimeException so Spring can easily catch it
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}