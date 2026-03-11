package com.project.jobportal.web.dto.employer.companyProfile;

import jakarta.validation.constraints.NotBlank;

public record CompanyProfileRequest(

        @NotBlank(message = "Company name cannot be blank")
        String companyName,

        String industry,
        String website,
        String location,
        String description
) {
}

