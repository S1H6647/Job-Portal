package com.project.jobportal.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User owner;

    private String headline;          // "Junior Java Developer"
    private String bio;
    private String location;
    private String resumeUrl;         // uploaded PDF path (Master resume)

    @ElementCollection
    private List<String> skills;      // ["Java", "Spring Boot"]

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;  // reuse your existing enum

    @CreationTimestamp
    private Instant createdAt;
}