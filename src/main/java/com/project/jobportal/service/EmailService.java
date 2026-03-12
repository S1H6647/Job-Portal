package com.project.jobportal.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // ─────────────────────────────────────────────
    // Core sender
    // ─────────────────────────────────────────────

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }

    // ─────────────────────────────────────────────
    // Shared HTML builder
    // ─────────────────────────────────────────────

    private String buildTemplate(String headerColor, String headerTitle,
                                 String headerEmoji, String bodyHtml) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="margin:0; padding:0; font-family: Arial, sans-serif; background-color:#f4f4f4;">
                  <table width="100%%" cellpadding="0" cellspacing="0">
                    <tr>
                      <td align="center" style="padding:40px 0;">
                        <table width="600" cellpadding="0" cellspacing="0"
                               style="background-color:#ffffff; border-radius:8px;
                                      box-shadow:0 2px 8px rgba(0,0,0,0.1);">
                
                          <!-- Header -->
                          <tr>
                            <td style="background-color:%s; padding:32px;
                                       border-radius:8px 8px 0 0; text-align:center;">
                              <div style="font-size:40px; margin-bottom:12px;">%s</div>
                              <h1 style="color:#ffffff; margin:0; font-size:22px;">%s</h1>
                            </td>
                          </tr>
                
                          <!-- Body -->
                          <tr>
                            <td style="padding:32px;">
                              %s
                              <hr style="border:none; border-top:1px solid #e5e7eb; margin:24px 0;">
                              <p style="color:#6b7280; font-size:13px; margin:0;">
                                If you have any questions, feel free to reply to this email.
                              </p>
                            </td>
                          </tr>
                
                          <!-- Footer -->
                          <tr>
                            <td style="background-color:#f9fafb; padding:20px;
                                       border-radius:0 0 8px 8px; text-align:center;">
                              <p style="color:#9ca3af; font-size:12px; margin:0;">
                                © 2026 Job Portal · All rights reserved.
                              </p>
                            </td>
                          </tr>
                
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.formatted(headerColor, headerEmoji, headerTitle, bodyHtml);
    }

    private String row(String text) {
        return "<p style=\"color:#374151; font-size:15px; margin:0 0 14px;\">%s</p>"
                .formatted(text);
    }

    private String highlight(String text, String color) {
        return "<strong style=\"color:%s;\">%s</strong>".formatted(color, text);
    }

    // ─────────────────────────────────────────────
    // 1. Company profile — Approved
    // ─────────────────────────────────────────────

    public void sendCompanyProfileApproved(String to, String companyName) {
        String subject = "Your Company Profile Has Been Approved — Job Portal";
        String body = buildTemplate(
                "#16a34a", "Company Profile Approved", "🏢",
                row("Dear " + highlight(companyName, "#16a34a") + ",") +
                        row("Great news! Your company profile has been reviewed and " +
                                highlight("approved", "#16a34a") + " by our admin team.") +
                        row("Your profile is now live on the Job Portal. You can start " +
                                "posting job openings and reaching qualified candidates right away.") +
                        row("Welcome aboard — we're excited to have you with us!")
        );
        sendEmail(to, subject, body);
    }

    // ─────────────────────────────────────────────
    // 2. Company profile — Rejected
    // ─────────────────────────────────────────────

    public void sendCompanyProfileRejected(String to, String companyName) {
        String subject = "Update on Your Company Profile — Job Portal";
        String body = buildTemplate(
                "#dc2626", "Company Profile Not Approved", "⚠️",
                row("Dear " + highlight(companyName, "#dc2626") + ",") +
                        row("Thank you for registering on Job Portal. After reviewing your " +
                                "company profile, we regret to inform you that it has " +
                                highlight("not been approved", "#dc2626") + " at this time.") +
                        row("This may be due to incomplete information, unverifiable details, " +
                                "or content that does not meet our platform guidelines.") +
                        row("Please review your profile, make the necessary updates, and " +
                                "resubmit for approval. Our team will review it again promptly.")
        );
        sendEmail(to, subject, body);
    }

    // ─────────────────────────────────────────────
    // 3. Job posting — Approved
    // ─────────────────────────────────────────────

    public void sendJobPostingApproved(String to, String companyName, String jobTitle) {
        String subject = "Your Job Posting is Now Live — Job Portal";
        String body = buildTemplate(
                "#2563eb", "Job Posting Approved", "✅",
                row("Dear " + highlight(companyName, "#2563eb") + ",") +
                        row("Your job posting for " + highlight(jobTitle, "#2563eb") +
                                " has been reviewed and " + highlight("approved", "#2563eb") + ".") +
                        row("The listing is now publicly visible to candidates on Job Portal. " +
                                "You can expect applications to start coming in shortly.") +
                        row("You can manage this posting, review applicants, and update " +
                                "the listing anytime from your employer dashboard.")
        );
        sendEmail(to, subject, body);
    }

    // ─────────────────────────────────────────────
    // 4. Job posting — Rejected
    // ─────────────────────────────────────────────

    public void sendJobPostingRejected(String to, String companyName, String jobTitle) {
        String subject = "Your Job Posting Was Not Approved — Job Portal";
        String body = buildTemplate(
                "#9333ea", "Job Posting Not Approved", "📋",
                row("Dear " + highlight(companyName, "#9333ea") + ",") +
                        row("Your job posting for " + highlight(jobTitle, "#9333ea") +
                                " has been reviewed and unfortunately " +
                                highlight("not approved", "#9333ea") + " at this time.") +
                        row("Common reasons include unclear job descriptions, missing requirements, " +
                                "or content that does not comply with our posting guidelines.") +
                        row("Please revisit the posting, make the necessary revisions, " +
                                "and resubmit. We are happy to help if you need guidance.")
        );
        sendEmail(to, subject, body);
    }

    // ─────────────────────────────────────────────
    // 5. Job application — Approved
    // ─────────────────────────────────────────────

    public void sendApplicationApproved(String to, String candidateName, String jobTitle,
                                        String companyName) {
        String subject = "Congratulations! Your Application Was Approved — Job Portal";
        String body = buildTemplate(
                "#0891b2", "Application Approved", "🎉",
                row("Dear " + highlight(candidateName, "#0891b2") + ",") +
                        row("We have great news! Your application for " +
                                highlight(jobTitle, "#0891b2") + " at " +
                                highlight(companyName, "#0891b2") + " has been " +
                                highlight("approved", "#0891b2") + ".") +
                        row("The hiring team was impressed with your profile and would like " +
                                "to move forward with you. You will be contacted shortly " +
                                "regarding the next steps in the process.") +
                        row("Best of luck — you've earned this opportunity! 🚀")
        );
        sendEmail(to, subject, body);
    }

    // ─────────────────────────────────────────────
    // 6. Job application — Rejected
    // ─────────────────────────────────────────────

    public void sendApplicationRejected(String to, String candidateName, String jobTitle,
                                        String companyName) {
        String subject = "Update on Your Application — Job Portal";
        String body = buildTemplate(
                "#6b7280", "Application Status Update", "📩",
                row("Dear " + highlight(candidateName, "#374151") + ",") +
                        row("Thank you for applying for " + highlight(jobTitle, "#6b7280") +
                                " at " + highlight(companyName, "#6b7280") + ".") +
                        row("After careful consideration, the hiring team has decided " +
                                highlight("not to move forward", "#6b7280") +
                                " with your application at this time. This was a competitive " +
                                "process with many strong candidates.") +
                        row("We encourage you not to be discouraged. Keep refining your " +
                                "profile and applying to roles that match your skills. " +
                                "The right opportunity is ahead of you. 💪")
        );
        sendEmail(to, subject, body);
    }
}