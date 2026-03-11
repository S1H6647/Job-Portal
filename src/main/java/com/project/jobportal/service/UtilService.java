package com.project.jobportal.service;

import com.project.jobportal.domain.*;
import com.project.jobportal.exception.ResourceNotFoundException;
import com.project.jobportal.repository.*;
import org.springframework.stereotype.Service;

@Service
public class UtilService {

    private final UserRepository userRepository;
    private final CompanyProfileRepository companyProfileRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    public UtilService(UserRepository userRepository, CompanyProfileRepository companyProfileRepository, JobPostingRepository jobPostingRepository, ApplicationRepository applicationRepository, CandidateProfileRepository candidateProfileRepository) {
        this.userRepository = userRepository;
        this.companyProfileRepository = companyProfileRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.applicationRepository = applicationRepository;
        this.candidateProfileRepository = candidateProfileRepository;
    }

    protected User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with %s not found", email)));
    }

    protected CompanyProfile findCompanyById(Long id) {
        return companyProfileRepository.findCompanyProfileById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company profile not found"));
    }

    protected JobPosting findJobById(Long id) {
        return jobPostingRepository.findJobPostingById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found"));
    }

    protected Application findApplicationById(Long id) {
        return applicationRepository.findApplicationsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application for not found"));
    }

    protected CandidateProfile findCandidateProfileById(Long id) {
        return candidateProfileRepository.findCandidateProfileById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));
    }

    protected Application findApplicationByCandidate(User candidate) {
        return applicationRepository.findApplicationsByCandidate(candidate)
                .orElseThrow(() -> new ResourceNotFoundException("You have currently no application"));
    }
}
