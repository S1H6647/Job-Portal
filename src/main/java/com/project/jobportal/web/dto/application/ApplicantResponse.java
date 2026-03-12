package com.project.jobportal.web.dto.application;

import com.project.jobportal.domain.Application;
import com.project.jobportal.domain.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ApplicantResponse(
        Long id,
        Long jobId,
        String jobTitle,
        String candidateName,
        String candidateEmail,
        String candidateHeadline,
        List<String> candidateSkills,
        String coverLetter,
        String resumeUrl,
        ApplicationStatus status,
        LocalDateTime appliedAt
) {
    public static ApplicantResponse from(Application application) {
        var profile = application.getCandidate().getCandidateProfile();
        return new ApplicantResponse(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                application.getCandidate().getName(),
                application.getCandidate().getEmail(),
                profile != null ? profile.getHeadline() : null,
                profile != null ? profile.getSkills() : List.of(),
                application.getCoverLetter(),
                application.getResumeUrl(),
                application.getStatus(),
                application.getAppliedAt()
        );
    }
}
