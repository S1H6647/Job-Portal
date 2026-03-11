package com.project.jobportal.service;

import com.project.jobportal.domain.Application;
import com.project.jobportal.domain.CompanyProfile;
import com.project.jobportal.domain.JobPosting;
import com.project.jobportal.domain.JobStatus;
import com.project.jobportal.repository.ApplicationRepository;
import com.project.jobportal.repository.JobPostingRepository;
import com.project.jobportal.security.user.UserPrincipal;
import com.project.jobportal.web.dto.application.ApplicantResponse;
import com.project.jobportal.web.dto.application.ApplicationStatusRequest;
import com.project.jobportal.web.dto.employer.job.JobRequest;
import com.project.jobportal.web.dto.employer.job.JobResponse;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {

    private final JobPostingRepository jobPostingRepository;
    private final UtilService utilService;
    private final ApplicationRepository applicationRepository;

    public JobService(JobPostingRepository jobPostingRepository, UtilService utilService, ApplicationRepository applicationRepository) {
        this.jobPostingRepository = jobPostingRepository;
        this.utilService = utilService;
        this.applicationRepository = applicationRepository;
    }

    public JobResponse postJob(Long companyId, JobRequest request, UserPrincipal userPrincipal) throws AccessDeniedException {
        CompanyProfile company = utilService.findCompanyById(companyId);

        if (!company.getOwner().getId().equals(userPrincipal.getId())) {
            throw new AccessDeniedException("You are not authorized to post jobs for this company");
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

        jobPostingRepository.save(job);
        return JobResponse.from(job);
    }

    public JobResponse rejectJob(Long jobId) {
        var job = utilService.findJobById(jobId);

        job.setStatus(JobStatus.REJECTED);

        jobPostingRepository.save(job);
        return JobResponse.from(job);
    }


    public List<ApplicantResponse> getAllApplications(Long jobId) {
        var job = utilService.findJobById(jobId);

        List<Application> applicationList = job.getApplications();

        return applicationList.stream()
                .map(ApplicantResponse::from)
                .toList();
    }

    public ApplicantResponse updateApplicationStatus(Long jobId, Long applicationId, ApplicationStatusRequest statusRequest) {
        utilService.findJobById(jobId);

        var application = utilService.findApplicationById(applicationId);

        application.setStatus(statusRequest.status());
        applicationRepository.save(application);
        return ApplicantResponse.from(application);
    }
}

