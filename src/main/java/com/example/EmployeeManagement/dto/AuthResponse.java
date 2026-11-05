
package com.example.EmployeeManagement.dto;

public class AuthResponse {

    private String accessToken;
    private String tokenType;
    private long expiresIn;

    public AuthResponse(
            String accessToken,
            String tokenType,
            long expiresIn) {

        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
