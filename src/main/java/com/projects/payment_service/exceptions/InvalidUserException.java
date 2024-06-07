package com.projects.payment_service.exceptions;

public class InvalidUserException extends Exception {
    public InvalidUserException(String message) {
        super(message);
    }
}
