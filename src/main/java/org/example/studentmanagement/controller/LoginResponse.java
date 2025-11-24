package org.example.studentmanagement.controller;

public class LoginResponse {

    private final String message;
    private final String redirectUrl;

    public LoginResponse(String message, String redirectUrl) {
        this.message = message;
        this.redirectUrl = redirectUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}


