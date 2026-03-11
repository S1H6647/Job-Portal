package com.project.jobportal.exception;

public class MoreThanOneCompanyException extends RuntimeException {
    public MoreThanOneCompanyException(String message) {
        super(message);
    }
}
