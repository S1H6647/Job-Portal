package com.project.jobportal.repository;

import com.project.jobportal.domain.JobPosting;
import com.project.jobportal.domain.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    List<JobPosting> findByStatus(JobStatus status);

    List<JobPosting> findByStatusAndExpiresAtBefore(JobStatus status, LocalDateTime expiresAtBefore);

    Optional<JobPosting> findJobPostingById(Long id);
}
