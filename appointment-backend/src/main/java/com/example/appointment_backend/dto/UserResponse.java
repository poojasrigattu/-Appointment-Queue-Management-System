
package com.example.appointment_backend.dto;

import com.example.appointment_backend.entity.User;

public class UserResponse {

    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String role;

    public UserResponse(
            Long id,
            String fullName,
            String phone,
            String email,
            String role) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getRole()
        );
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
