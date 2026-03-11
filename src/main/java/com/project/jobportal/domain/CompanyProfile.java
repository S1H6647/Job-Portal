package com.project.jobportal.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfile {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User owner;                    // linked to their account

    @Column(nullable = false)
    private String companyName;

    private String industry;              // "Tech", "Finance", "Healthcare"
    private String website;
    private String location;
    private String description;

    @Enumerated(EnumType.STRING)
    private ProfileStatus status;         // PENDING, APPROVED, REJECTED (admin approves)
}
