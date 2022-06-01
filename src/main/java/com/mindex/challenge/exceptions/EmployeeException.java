package com.mindex.challenge.exceptions;

public class EmployeeException extends Exception {

    public static final String INVALID_EMPLOYEE = "Invalid employee";

    public EmployeeException(String errorMessage) {
        super(errorMessage);
    }
}
