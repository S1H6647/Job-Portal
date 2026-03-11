package com.project.jobportal.service;

import com.project.jobportal.domain.JobStatus;
import com.project.jobportal.repository.JobPostingRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JobExpiryScheduler {
    private final JobPostingRepository jobPostingRepository;

    public JobExpiryScheduler(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Kathmandu")
    @Transactional
    public void expireOutdatedJobs() {
        var expired = jobPostingRepository.findByStatusAndExpiresAtBefore(
                JobStatus.ACTIVE,
                LocalDateTime.now()
        );

        expired.forEach(job -> job.setStatus(JobStatus.EXPIRED));
        jobPostingRepository.saveAll(expired);

        System.out.println(("Expired {} jobs at {}" + expired.size() + LocalDateTime.now()));
    }
}
