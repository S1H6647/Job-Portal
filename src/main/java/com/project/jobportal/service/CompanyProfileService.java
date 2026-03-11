package com.project.jobportal.service;

import com.project.jobportal.domain.CompanyProfile;
import com.project.jobportal.domain.ProfileStatus;
import com.project.jobportal.exception.MoreThanOneCompanyException;
import com.project.jobportal.repository.CompanyProfileRepository;
import com.project.jobportal.security.user.UserPrincipal;
import com.project.jobportal.web.dto.employer.companyProfile.CompanyProfileRequest;
import com.project.jobportal.web.dto.employer.companyProfile.CompanyProfileResponse;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class CompanyProfileService {

    private final CompanyProfileRepository companyProfileRepository;
    private final UtilService utilService;

    public CompanyProfileService(CompanyProfileRepository companyProfileRepository, UtilService utilService) {
        this.companyProfileRepository = companyProfileRepository;
        this.utilService = utilService;
    }

    public CompanyProfileResponse setUpCompany(CompanyProfileRequest request, UserPrincipal userPrincipal) {
        var user = userPrincipal.getUser();

        if (companyProfileRepository.existsByOwner(user)) {
            throw new MoreThanOneCompanyException("You already have a company profile");
        }

        CompanyProfile companyProfile = new CompanyProfile();
        companyProfile.setCompanyName(request.companyName());
        companyProfile.setIndustry(request.industry());
        companyProfile.setWebsite(request.website());
        companyProfile.setLocation(request.location());
        companyProfile.setDescription(request.description());
        companyProfile.setStatus(ProfileStatus.PENDING);
        companyProfile.setOwner(userPrincipal.getUser());

        companyProfileRepository.save(companyProfile);

        return CompanyProfileResponse.from(companyProfile);
    }

    public CompanyProfileResponse updateCompanyProfile(Long companyId, CompanyProfileRequest request, UserPrincipal userPrincipal) throws AccessDeniedException {
        var companyProfile = utilService.findCompanyById(companyId);

        if (!companyProfile.getOwner().getId().equals(userPrincipal.getId())) {
            throw new AccessDeniedException("You are not authorized to update this company profile");
        }

        companyProfile.setCompanyName(request.companyName());
        companyProfile.setIndustry(request.industry());
        companyProfile.setWebsite(request.website());
        companyProfile.setLocation(request.location());
        companyProfile.setDescription(request.description());

        companyProfileRepository.save(companyProfile);

        return CompanyProfileResponse.from(companyProfile);
    }

    public List<CompanyProfileResponse> getAllCompanies() {
        var companies = companyProfileRepository.findByStatus(ProfileStatus.APPROVED);
        return companies.stream()
                .map(CompanyProfileResponse::from)
                .toList();
    }

    public List<CompanyProfileResponse> getAllPendingCompanies() {
        var companies = companyProfileRepository.findByStatus(ProfileStatus.PENDING);
        return companies.stream()
                .map(CompanyProfileResponse::from)
                .toList();
    }

    public CompanyProfileResponse approveCompany(Long companyId) {
        var companyProfile = utilService.findCompanyById(companyId);
        companyProfile.setStatus(ProfileStatus.APPROVED);
        companyProfileRepository.save(companyProfile);
        return CompanyProfileResponse.from(companyProfile);
    }

    public CompanyProfileResponse rejectCompany(Long companyId) {
        var companyProfile = utilService.findCompanyById(companyId);
        companyProfile.setStatus(ProfileStatus.REJECTED);
        companyProfileRepository.save(companyProfile);
        return CompanyProfileResponse.from(companyProfile);
    }
}
