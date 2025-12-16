package org.example.studentmanagement.dto;

public class JwtAuthenticationResponse {

    private final String token;
    private final String tokenType = "Bearer";

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }
}

