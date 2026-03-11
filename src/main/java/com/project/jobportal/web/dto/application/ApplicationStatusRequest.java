package com.project.jobportal.web.dto.application;

import com.project.jobportal.domain.ApplicationStatus;

public record ApplicationStatusRequest(
        ApplicationStatus status   // REVIEWED, HIRED, or REJECTED
) {
}
