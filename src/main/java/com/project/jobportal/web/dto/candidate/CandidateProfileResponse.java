package com.project.jobportal.web.dto.candidate;

import com.project.jobportal.domain.CandidateProfile;
import com.project.jobportal.domain.ExperienceLevel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CandidateProfileResponse(

        Long id,
        UUID userId,
        String headline,
        String bio,
        String location,
        String resumeUrl,
        List<String> skills,
        ExperienceLevel experienceLevel,
        Instant createdAt
) {
    public static CandidateProfileResponse from(CandidateProfile profile) {
        return new CandidateProfileResponse(
                profile.getId(),
                profile.getOwner().getId(),
                profile.getHeadline(),
                profile.getBio(),
                profile.getLocation(),
                profile.getResumeUrl(),
                profile.getSkills(),
                profile.getExperienceLevel(),
                profile.getCreatedAt()
        );
    }
}

