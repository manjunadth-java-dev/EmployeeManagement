
package com.example.EmployeeManagement.dto;

public class RegisterResponse {

    private Long id;
    private String username;
    private String role;
    private String message;

    public RegisterResponse(
            Long id,
            String username,
            String role,
            String message) {

        this.id = id;
        this.username = username;
        this.role = role;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }
}
