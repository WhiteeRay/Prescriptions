package com.example.prescriptions.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }

    public NotFoundException(String resourceName, Long id){
        super(String.format("%s with id %d not found", resourceName, id));
    }
}