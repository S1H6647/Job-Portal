package com.project.jobportal.repository;

import com.project.jobportal.domain.CandidateProfile;
import com.project.jobportal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {
    Optional<CandidateProfile> findCandidateProfileById(Long id);

    Optional<CandidateProfile> findByOwner(User owner);

    boolean existsByOwner(User owner);
}

