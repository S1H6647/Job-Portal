package com.project.jobportal.service;

import com.project.jobportal.domain.*;
import com.project.jobportal.exception.AuthorityException;
import com.project.jobportal.repository.ApplicationRepository;
import com.project.jobportal.repository.JobPostingRepository;
import com.project.jobportal.security.user.UserPrincipal;
import com.project.jobportal.web.dto.application.ApplicantResponse;
import com.project.jobportal.web.dto.application.ApplicationStatusRequest;
import com.project.jobportal.web.dto.employer.job.JobRequest;
import com.project.jobportal.web.dto.employer.job.JobResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {

    private final JobPostingRepository jobPostingRepository;
    private final UtilService utilService;
    private final ApplicationRepository applicationRepository;
    private final EmailService emailService;

    public JobService(JobPostingRepository jobPostingRepository, UtilService utilService, ApplicationRepository applicationRepository, EmailService emailService) {
        this.jobPostingRepository = jobPostingRepository;
        this.utilService = utilService;
        this.applicationRepository = applicationRepository;
        this.emailService = emailService;
    }

    public JobResponse postJob(Long companyId, JobRequest request, UserPrincipal userPrincipal) throws AccessDeniedException {
        CompanyProfile company = utilService.findCompanyById(companyId);

        if (!company.getOwner().getId().equals(userPrincipal.getId())) {
            throw new AccessDeniedException("You are not authorized to post jobs for this company");
        }

        if (company.getStatus() != ProfileStatus.APPROVED) {
            throw new AuthorityException("Your company profile must be approved by an administrator before you can post jobs.");
        }

        JobPosting job = new JobPosting();
        job.setCompany(company);
        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setLocation(request.location());
        job.setEmploymentType(request.employmentType());
        job.setExperienceLevel(request.experienceLevel());
        job.setRequiredSkills(request.requiredSkills());
        job.setStatus(JobStatus.PENDING_APPROVAL);
        job.setCreatedAt(Instant.now());

        jobPostingRepository.save(job);
        return JobResponse.from(job);
    }

    public List<JobResponse> getAllActiveJobs() {
        var jobPosting = jobPostingRepository.findByStatus(JobStatus.ACTIVE);
        return jobPosting.stream()
                .map(JobResponse::from)
                .toList();
    }

    public JobResponse getJobById(Long jobId) {
        var job = utilService.findJobById(jobId);
        return JobResponse.from(job);
    }

    public List<JobResponse> getJobsByEmployer(UserPrincipal userPrincipal) {
        var user = userPrincipal.getUser();
        var company = user.getCompanyProfile();

        if (company == null) {
            return List.of();
        }

        var jobs = jobPostingRepository.findByCompany(company);
        return jobs.stream()
                .map(JobResponse::from)
                .toList();
    }

    public JobResponse editJob(Long companyId, JobRequest request, Long jobId, UserPrincipal userPrincipal) throws AccessDeniedException {
        var company = utilService.findCompanyById(companyId);

        if (!company.getOwner().getId().equals(userPrincipal.getId())) {
            throw new AccessDeniedException("You are not authorized to edit jobs for this company");
        }

        var job = utilService.findJobById(jobId);
        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setLocation(request.location());
        job.setEmploymentType(request.employmentType());
        job.setExperienceLevel(request.experienceLevel());

        jobPostingRepository.save(job);

        return JobResponse.from(job);
    }

    public void deleteJob(Long companyId, Long jobId, UserPrincipal userPrincipal) throws AccessDeniedException {
        var company = utilService.findCompanyById(companyId);

        if (!company.getOwner().getId().equals(userPrincipal.getId())) {
            throw new AccessDeniedException("You are not authorized to delete jobs for this company");
        }

        var job = utilService.findJobById(jobId);
        jobPostingRepository.delete(job);
    }

    // Admin -> Job
    public List<JobResponse> pendingJobs() {
        var jobList = jobPostingRepository.findByStatus(JobStatus.PENDING_APPROVAL);
        return jobList.stream()
                .map(JobResponse::from)
                .toList();
    }

    public JobResponse approveJob(Long jobId) {
        var job = utilService.findJobById(jobId);

        job.setStatus(JobStatus.ACTIVE);
        job.setApprovedAt(LocalDateTime.now());
        job.setExpiresAt(LocalDateTime.now().plusDays(30));

        var owner = job.getCompany().getOwner().getEmail();
        var companyName = job.getCompany().getCompanyName();
        var jobTitle = job.getTitle();
        emailService.sendJobPostingApproved(owner, companyName, jobTitle);

        jobPostingRepository.save(job);
        return JobResponse.from(job);
    }

    public JobResponse rejectJob(Long jobId) {
        var job = utilService.findJobById(jobId);

        job.setStatus(JobStatus.REJECTED);

        var owner = job.getCompany().getOwner().getEmail();
        var companyName = job.getCompany().getCompanyName();
        var jobTitle = job.getTitle();
        emailService.sendJobPostingRejected(owner, companyName, jobTitle);

        jobPostingRepository.save(job);
        return JobResponse.from(job);
    }


    @Transactional(readOnly = true)
    public List<ApplicantResponse> getAllApplications(Long jobId) {
        var job = utilService.findJobById(jobId);

        List<Application> applicationList = job.getApplications();

        return applicationList.stream()
                .map(ApplicantResponse::from)
                .toList();
    }

    @Transactional
    public ApplicantResponse updateApplicationStatus(Long jobId, Long applicationId, ApplicationStatusRequest statusRequest) {
        var job = utilService.findJobById(jobId);

        var application = utilService.findApplicationById(applicationId);

        application.setStatus(statusRequest.status());

        String email = application.getCandidate().getEmail();
        String candidateName = application.getCandidate().getName();
        String jobTitle = job.getTitle();
        String companyName = job.getCompany().getCompanyName();

        if (statusRequest.status() == ApplicationStatus.HIRED) {
            emailService.sendApplicationApproved(email, candidateName, jobTitle, companyName);
        } else if (statusRequest.status() == ApplicationStatus.REJECTED) {
            emailService.sendApplicationRejected(email, candidateName, jobTitle, companyName);
        }


        applicationRepository.save(application);
        return ApplicantResponse.from(application);
    }
}

