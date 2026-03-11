package com.project.jobportal.web.controller;

import com.project.jobportal.security.user.UserPrincipal;
import com.project.jobportal.service.CompanyProfileService;
import com.project.jobportal.service.JobService;
import com.project.jobportal.web.dto.application.ApplicantResponse;
import com.project.jobportal.web.dto.application.ApplicationStatusRequest;
import com.project.jobportal.web.dto.employer.companyProfile.CompanyProfileRequest;
import com.project.jobportal.web.dto.employer.companyProfile.CompanyProfileResponse;
import com.project.jobportal.web.dto.employer.job.JobRequest;
import com.project.jobportal.web.dto.employer.job.JobResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/employer")
public class EmployerController {

    private final CompanyProfileService companyProfileService;
    private final JobService jobService;

    public EmployerController(CompanyProfileService companyProfileService, JobService jobService) {
        this.companyProfileService = companyProfileService;
        this.jobService = jobService;
    }

    @PostMapping("/profile")
    public ResponseEntity<CompanyProfileResponse> setUpCompany(
            @Valid @RequestBody CompanyProfileRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        var response = companyProfileService.setUpCompany(request, userPrincipal);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/profile/{companyId}")
    public ResponseEntity<CompanyProfileResponse> updateCompanyProfile(
            @PathVariable Long companyId,
            @RequestBody CompanyProfileRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) throws AccessDeniedException {
        var response = companyProfileService.updateCompanyProfile(companyId, request, userPrincipal);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{companyId}/jobs")
    public ResponseEntity<JobResponse> postJob(
            @PathVariable Long companyId,
            @RequestBody JobRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) throws AccessDeniedException {
        var response = jobService.postJob(companyId, request, userPrincipal);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponse>> viewMyJobs() {
        var response = jobService.getAllActiveJobs();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/jobs/{companyId}/{jobId}")
    public ResponseEntity<JobResponse> postJob(
            @PathVariable Long jobId,
            @PathVariable Long companyId,
            @RequestBody JobRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) throws AccessDeniedException {
        var response = jobService.editJob(companyId, request, jobId, userPrincipal);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/jobs/{companyId}/{jobId}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long jobId,
            @PathVariable Long companyId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) throws AccessDeniedException {
        jobService.deleteJob(companyId, jobId, userPrincipal);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobs/{jobId}/applicants")
    public ResponseEntity<List<ApplicantResponse>> getAllApplications(
            @PathVariable Long jobId
    ) {
        var response = jobService.getAllApplications(jobId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/jobs/{jobId}/applicants/{applicationId}")
    public ResponseEntity<ApplicantResponse> updateApplicationStatus(
            @PathVariable Long jobId,
            @PathVariable Long applicationId,
            @RequestBody ApplicationStatusRequest statusRequest
    ) {
        var response = jobService.updateApplicationStatus(jobId, applicationId, statusRequest);
        return ResponseEntity.ok(response);
    }
}
