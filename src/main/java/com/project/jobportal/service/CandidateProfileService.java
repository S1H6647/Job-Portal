package com.project.jobportal.service;

import com.project.jobportal.domain.Application;
import com.project.jobportal.domain.CandidateProfile;
import com.project.jobportal.domain.JobStatus;
import com.project.jobportal.exception.DuplicateApplicantException;
import com.project.jobportal.exception.InactiveJobException;
import com.project.jobportal.exception.NoCandidateProfileException;
import com.project.jobportal.exception.ResourceNotFoundException;
import com.project.jobportal.repository.ApplicationRepository;
import com.project.jobportal.repository.CandidateProfileRepository;
import com.project.jobportal.security.user.UserPrincipal;
import com.project.jobportal.web.dto.application.ApplicantResponse;
import com.project.jobportal.web.dto.application.ApplicationRequest;
import com.project.jobportal.web.dto.candidate.CandidateProfileRequest;
import com.project.jobportal.web.dto.candidate.CandidateProfileResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UtilService utilService;
    private final ApplicationRepository applicationRepository;
    private final StorageService storageService;

    public CandidateProfileService(CandidateProfileRepository candidateProfileRepository, UtilService utilService, ApplicationRepository applicationRepository, StorageService storageService) {
        this.candidateProfileRepository = candidateProfileRepository;
        this.utilService = utilService;
        this.applicationRepository = applicationRepository;
        this.storageService = storageService;
    }

    public CandidateProfileResponse createProfile(CandidateProfileRequest request, UserPrincipal userPrincipal) {
        CandidateProfile profile = new CandidateProfile();

        profile.setOwner(userPrincipal.getUser());
        profile.setHeadline(request.headline());
        profile.setBio(request.bio());
        profile.setLocation(request.location());
        profile.setResumeUrl(request.resumeUrl());
        profile.setSkills(request.skills());
        profile.setExperienceLevel(request.experienceLevel());

        candidateProfileRepository.save(profile);

        return CandidateProfileResponse.from(profile);
    }

    public CandidateProfileResponse getProfile(UserPrincipal userPrincipal) {
        var profile = candidateProfileRepository.findByOwner(userPrincipal.getUser())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));
        return CandidateProfileResponse.from(profile);
    }

    public CandidateProfileResponse updateProfile(Long profileId, CandidateProfileRequest request, UserPrincipal userPrincipal) throws AccessDeniedException {
        CandidateProfile existingProfile = utilService.findCandidateProfileById(profileId);

        if (!existingProfile.getOwner().getId().equals(userPrincipal.getId())) {
            throw new AccessDeniedException("You do not have permission to edit this profile.");
        }

        existingProfile.setHeadline(request.headline());
        existingProfile.setBio(request.bio());
        existingProfile.setLocation(request.location());
        existingProfile.setResumeUrl(request.resumeUrl());
        existingProfile.setSkills(request.skills());
        existingProfile.setExperienceLevel(request.experienceLevel());

        candidateProfileRepository.save(existingProfile);
        return CandidateProfileResponse.from(existingProfile);
    }

    public String uploadResume(MultipartFile file, UserPrincipal userPrincipal) throws IOException {
        String resumeUrl = storageService.uploadResume(file);
        var user = userPrincipal.getUser();
        var application = user.getCandidateProfile();
        application.setResumeUrl(resumeUrl);
        candidateProfileRepository.save(application);

        return "File successfully uploaded";
    }

    public List<ApplicantResponse> viewMyApplications(UserPrincipal userPrincipal) {
        var applications = utilService.findApplicationsByCandidate(userPrincipal.getUser());

        return applications.stream().map(ApplicantResponse::from).toList();
    }

    public ApplicantResponse applyToJob(Long jobId, UserPrincipal userPrincipal, ApplicationRequest request) {
        var candidate = utilService.findUserByEmail(userPrincipal.getEmail());
        if (candidate.getCandidateProfile() == null) {
            throw new NoCandidateProfileException("Please complete your profile before applying");
        }

        var job = utilService.findJobById(jobId);
        if (job.getStatus() != JobStatus.ACTIVE) {
            throw new InactiveJobException("Job is not longer accepting applications");
        }

        if (applicationRepository.existsByJobAndCandidate(job, candidate)) {
            throw new DuplicateApplicantException("You have already applied for this position");
        }

        var application = new Application();
        application.setCandidate(candidate);
        application.setJob(job);
        application.setCoverLetter(request.coverLetter());
        application.setResumeUrl(candidate.getCandidateProfile().getResumeUrl());

        applicationRepository.save(application);
        return ApplicantResponse.from(application);
    }
}

