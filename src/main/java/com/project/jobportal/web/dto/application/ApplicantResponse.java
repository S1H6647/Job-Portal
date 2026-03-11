package com.project.jobportal.web.dto.application;

import com.project.jobportal.domain.Application;
import com.project.jobportal.domain.ApplicationStatus;

import java.time.LocalDateTime;

public record ApplicantResponse(
        String jobTitle,
        String candidateName,
        String candidateEmail,
        ApplicationStatus status,
        LocalDateTime appliedAt
) {
    public static ApplicantResponse from(Application application) {
        return new ApplicantResponse(
                application.getJob().getTitle(),
                application.getCandidate().getName(),
                application.getCandidate().getEmail(),
                application.getStatus(),
                application.getAppliedAt()
        );
    }
}
