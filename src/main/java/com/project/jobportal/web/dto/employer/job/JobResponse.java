package com.project.jobportal.web.dto.employer.job;

import com.project.jobportal.domain.EmploymentType;
import com.project.jobportal.domain.ExperienceLevel;
import com.project.jobportal.domain.JobPosting;
import com.project.jobportal.domain.JobStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record JobResponse(

        Long id,
        Long companyId,
        String title,
        String description,
        String location,
        EmploymentType employmentType,
        ExperienceLevel experienceLevel,
        List<String> requiredSkills,
        JobStatus status,
        Instant createdAt,
        LocalDateTime approvedAt,
        LocalDateTime expiresAt
) {
    public static JobResponse from(JobPosting job) {
        return new JobResponse(
                job.getId(),
                job.getCompany() != null ? job.getCompany().getId() : null,
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getEmploymentType(),
                job.getExperienceLevel(),
                job.getRequiredSkills(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getApprovedAt(),
                job.getExpiresAt()
        );
    }
}
