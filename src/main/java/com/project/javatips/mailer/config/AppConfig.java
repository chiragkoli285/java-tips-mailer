package com.project.javatips.mailer.config;

public class AppConfig {

    private final String geminiApiKey;
    private final String gmailUser;
    private final String gmailPassword;
    private final String recipientEmail;

    public AppConfig() {
        // Reads from environment variables (set in GitHub Actions secrets)
        this.geminiApiKey    = getRequired("GEMINI_API_KEY");
        this.gmailUser       = getRequired("GMAIL_USER");
        this.gmailPassword   = getRequired("GMAIL_APP_PASSWORD");
        this.recipientEmail  = getRequired("RECIPIENT_EMAIL");
    }

    private String getRequired(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new RuntimeException("Missing required env variable: " + key);
        }
        return value;
    }

    public String getGeminiApiKey()    { return geminiApiKey; }
    public String getGmailUser()       { return gmailUser; }
    public String getGmailPassword()   { return gmailPassword; }
    public String getRecipientEmail()  { return recipientEmail; }
}