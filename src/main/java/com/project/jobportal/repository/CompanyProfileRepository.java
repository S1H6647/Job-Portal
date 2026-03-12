package com.project.jobportal.repository;

import com.project.jobportal.domain.CompanyProfile;
import com.project.jobportal.domain.ProfileStatus;
import com.project.jobportal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {
    Optional<CompanyProfile> findCompanyProfileById(Long id);

    boolean existsByOwner(User owner);

    List<CompanyProfile> findByStatus(ProfileStatus status);

    Optional<CompanyProfile> findByOwner(User owner);
}
