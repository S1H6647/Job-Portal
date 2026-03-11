package com.project.jobportal.web.controller;

import com.project.jobportal.service.CompanyProfileService;
import com.project.jobportal.service.JobService;
import com.project.jobportal.service.UserService;
import com.project.jobportal.web.dto.auth.UserResponse;
import com.project.jobportal.web.dto.employer.companyProfile.CompanyProfileResponse;
import com.project.jobportal.web.dto.employer.job.JobResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final CompanyProfileService companyProfileService;
    private final JobService jobService;

    public AdminController(UserService userService, CompanyProfileService companyProfileService, JobService jobService) {
        this.userService = userService;
        this.companyProfileService = companyProfileService;
        this.jobService = jobService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/companies")
    public ResponseEntity<List<CompanyProfileResponse>> getAllApprovedCompanies() {
        return ResponseEntity.ok(companyProfileService.getAllCompanies());
    }

    @GetMapping("/companies/pending")
    public ResponseEntity<List<CompanyProfileResponse>> getPendingCompanies() {
        return ResponseEntity.ok(companyProfileService.getAllPendingCompanies());
    }

    @PutMapping("/companies/{companyId}/approve")
    public ResponseEntity<CompanyProfileResponse> approveCompany(
            @PathVariable Long companyId
    ) {
        var response = companyProfileService.approveCompany(companyId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/companies/{companyId}/reject")
    public ResponseEntity<CompanyProfileResponse> rejectCompany(
            @PathVariable Long companyId
    ) {
        var response = companyProfileService.rejectCompany(companyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/jobs/pending")
    public ResponseEntity<List<JobResponse>> getPendingJobs() {
        return ResponseEntity.ok(jobService.pendingJobs());
    }

    @PutMapping("/jobs/{jobId}/approve")
    public ResponseEntity<JobResponse> approveJob(
            @PathVariable Long jobId
    ) {
        var response = jobService.approveJob(jobId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PutMapping("/jobs/{jobId}/reject")
    public ResponseEntity<JobResponse> rejectJob(
            @PathVariable Long jobId
    ) {
        var response = jobService.rejectJob(jobId);
        return ResponseEntity.ok(response);
    }

}
