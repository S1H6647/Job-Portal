package com.project.jobportal.web.controller;

import com.project.jobportal.security.user.UserPrincipal;
import com.project.jobportal.service.CandidateProfileService;
import com.project.jobportal.web.dto.application.ApplicantResponse;
import com.project.jobportal.web.dto.application.ApplicationRequest;
import com.project.jobportal.web.dto.candidate.CandidateProfileRequest;
import com.project.jobportal.web.dto.candidate.CandidateProfileResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    private final CandidateProfileService candidateProfileService;

    public CandidateController(CandidateProfileService candidateProfileService) {
        this.candidateProfileService = candidateProfileService;
    }

    @PostMapping("/profile")
    public ResponseEntity<CandidateProfileResponse> createProfile(
            @Valid @RequestBody CandidateProfileRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        var response = candidateProfileService.createProfile(request, userPrincipal);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<CandidateProfileResponse> getProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        var response = candidateProfileService.getProfile(userPrincipal);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile/{profileId}")
    public ResponseEntity<CandidateProfileResponse> updateProfile(
            @PathVariable Long profileId,
            @Valid @RequestBody CandidateProfileRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) throws AccessDeniedException {
        var response = candidateProfileService.updateProfile(profileId, request, userPrincipal);
        return ResponseEntity.ok(response);
    }

    // POST/candidate/resume -> Upload resume (PDF, multipart)
    @PostMapping("/resume")
    public ResponseEntity<String> uploadResume(
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) throws IOException {
        var response = candidateProfileService.uploadResume(file, userPrincipal);
        return ResponseEntity.ok(response);
    }

    // - `GET /candidate/applications` - View my applications
    @GetMapping("/application")
    public ResponseEntity<ApplicantResponse> viewMyApplication(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        var response = candidateProfileService.viewMyApplication(userPrincipal);
        return ResponseEntity.ok(response);
    }

    // POST /candidate/jobs/{jobId}/apply` - Apply to a job
    @PostMapping("/jobs/{jobId}/apply")
    public ResponseEntity<ApplicantResponse> applyToJob(
            @PathVariable Long jobId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody ApplicationRequest request
    ) {
        var response = candidateProfileService.applyToJob(jobId, userPrincipal, request);
        return ResponseEntity.status(201).body(response);
    }
}
