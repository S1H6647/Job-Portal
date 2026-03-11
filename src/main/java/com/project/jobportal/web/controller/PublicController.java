package com.project.jobportal.web.controller;

import com.project.jobportal.service.CompanyProfileService;
import com.project.jobportal.service.JobService;
import com.project.jobportal.web.dto.employer.companyProfile.CompanyProfileResponse;
import com.project.jobportal.web.dto.employer.job.JobResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PublicController {

    private final JobService jobService;
    private final CompanyProfileService companyProfileService;

    public PublicController(JobService jobService, CompanyProfileService companyProfileService) {
        this.jobService = jobService;
        this.companyProfileService = companyProfileService;
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponse>> getAllActiveJobs() {
        return ResponseEntity.ok(jobService.getAllActiveJobs());
    }

    @GetMapping("/company")
    public ResponseEntity<List<CompanyProfileResponse>> getAllApprovedCompanies() {
        return ResponseEntity.ok(companyProfileService.getAllCompanies());
    }
}
