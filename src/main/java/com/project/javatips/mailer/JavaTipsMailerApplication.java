package com.project.javatips.mailer;

import com.project.javatips.mailer.config.AppConfig;
import com.project.javatips.mailer.model.JavaTip;
import com.project.javatips.mailer.service.EmailFormatterService;
import com.project.javatips.mailer.service.EmailService;
import com.project.javatips.mailer.service.GeminiAIService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class JavaTipsMailerApplication {

	public static void main(String[] args) {
        System.out.println("Starting Java Tips Mailer...");

        try {
            AppConfig config = new AppConfig();
            GeminiAIService geminiService = new GeminiAIService(config);
            EmailFormatterService formatter = new EmailFormatterService();
            EmailService emailService = new EmailService(config);

            // Step 1: Fetch tips from Gemini AI
            System.out.println("Fetching Java tips from Gemini AI...");
            List<JavaTip> tips = geminiService.fetchDailyTips();

            if (tips == null || tips.isEmpty()) {
                System.out.println("No tips received. Exiting.");
                return;
            }

            System.out.println("Received " + tips.size() + " tips.");

            // Step 2: Format into HTML email
            String htmlBody = formatter.buildHtmlEmail(tips);

            // Step 3: Send email
            System.out.println("Sending email...");
            emailService.sendEmail(
                    config.getRecipientEmail(),
                    "☕ Your Daily Java Tips — " + java.time.LocalDate.now(),
                    htmlBody
            );

            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
	}

}
