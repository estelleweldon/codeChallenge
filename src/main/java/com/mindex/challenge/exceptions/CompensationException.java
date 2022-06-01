package com.mindex.challenge.exceptions;

public class CompensationException extends Exception {

    public static final String INVALID_COMPENSATION = "Invalid compensation";

    public CompensationException(String errorMessage) {
        super(errorMessage);
    }
}
