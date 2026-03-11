package com.project.jobportal.web.dto.auth;

import com.project.jobportal.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters")
        String password,

        @NotNull(message = "Role cannot be null")
        Role role
) {
}
