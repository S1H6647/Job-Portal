package com.project.jobportal.repository;

import com.project.jobportal.domain.Application;
import com.project.jobportal.domain.JobPosting;
import com.project.jobportal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findApplicationsById(Long id);

    List<Application> findApplicationsByCandidate(User candidate);

    boolean existsByJobAndCandidate(JobPosting job, User candidate);
}
