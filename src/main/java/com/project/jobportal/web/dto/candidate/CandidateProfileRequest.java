package com.project.jobportal.web.dto.candidate;

import com.project.jobportal.domain.ExperienceLevel;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CandidateProfileRequest(

        @NotBlank(message = "Headline cannot be blank")
        String headline,

        String bio,
        String location,
        String resumeUrl,
        List<String> skills,
        ExperienceLevel experienceLevel
) {
}

