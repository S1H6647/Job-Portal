package com.project.jobportal.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPosting {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyProfile company;

    private String title;                 // "Senior Java Developer"
    private String description;
    private String location;             // "Kathmandu" or "Remote"

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;       // FULL_TIME, PART_TIME, CONTRACT

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;      // JUNIOR, MID, SENIOR

    @ElementCollection
    private List<String> requiredSkills; // ["Java", "Spring Boot", "PostgreSQL"]

    @Enumerated(EnumType.STRING)
    private JobStatus status;            // PENDING_APPROVAL, ACTIVE, EXPIRED, CLOSED

    private Instant createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime expiresAt;     // auto-expired by @Scheduled after 30 days

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();
}
