package com.example.flightreservation.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * def retuns the exception with default argument.
     *
     * @return ResourceNotFoundException
     */
    public static ResourceNotFoundException def() {
        return new ResourceNotFoundException("resource not found");
    }
}