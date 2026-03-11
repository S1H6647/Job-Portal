package com.project.jobportal.web.dto.employer.companyProfile;

import com.project.jobportal.domain.CompanyProfile;
import com.project.jobportal.domain.ProfileStatus;

import java.util.UUID;

public record CompanyProfileResponse(

        Long id,
        UUID userId,
        String companyName,
        String industry,
        String website,
        String location,
        String description,
        ProfileStatus status
) {
    public static CompanyProfileResponse from(CompanyProfile profile) {
        return new CompanyProfileResponse(
                profile.getId(),
                profile.getOwner().getId(),
                profile.getCompanyName(),
                profile.getIndustry(),
                profile.getWebsite(),
                profile.getLocation(),
                profile.getDescription(),
                profile.getStatus()
        );
    }
}

