package com.example.prescriptions.exception;

public class ValidationException extends RuntimeException{
    
    public ValidationException(String message){
        super(message);
    }
}