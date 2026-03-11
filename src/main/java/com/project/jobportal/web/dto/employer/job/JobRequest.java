package com.project.jobportal.web.dto.employer.job;

import com.project.jobportal.domain.EmploymentType;
import com.project.jobportal.domain.ExperienceLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record JobRequest(

        @NotBlank(message = "Job title is required")
        String title,

        @NotBlank(message = "Job description is required")
        String description,

        @NotBlank(message = "Location is required")
        String location,

        @NotNull(message = "Employment type is required")
        EmploymentType employmentType,

        ExperienceLevel experienceLevel,
        List<String> requiredSkills
) {
}
