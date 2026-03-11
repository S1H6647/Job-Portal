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

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UtilService utilService;
    private final ApplicationRepository applicationRepository;

    public CandidateProfileService(CandidateProfileRepository candidateProfileRepository, UtilService utilService, ApplicationRepository applicationRepository) {
        this.candidateProfileRepository = candidateProfileRepository;
        this.utilService = utilService;
        this.applicationRepository = applicationRepository;
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
        String uploadDir = "src/main/java/com/project/jobportal/uploads";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            System.out.println(directory.mkdirs());
        }
        Path filePath = Paths.get(uploadDir + "/" + file.getOriginalFilename());
        Files.write(filePath, file.getBytes());

        var user = userPrincipal.getUser();
        var application = user.getCandidateProfile();
        application.setResumeUrl(filePath.toString());
        candidateProfileRepository.save(application);

        return "File successfully uploaded";
    }

    public ApplicantResponse viewMyApplication(UserPrincipal userPrincipal) {
        var application = utilService.findApplicationByCandidate(userPrincipal.getUser());

        return ApplicantResponse.from(application);
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

