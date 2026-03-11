package com.project.jobportal.web.dto.auth;

import com.project.jobportal.domain.Role;
import com.project.jobportal.domain.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        Role role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}
