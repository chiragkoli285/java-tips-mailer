package com.project.javatips.mailer.service;

import com.javatipper.config.AppConfig;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {

    private final AppConfig config;

    public EmailService(AppConfig config) {
        this.config = config;
    }

    public void sendEmail(String to, String subject, String htmlBody)
            throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        config.getGmailUser(),
                        config.getGmailPassword()
                );
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(config.getGmailUser()));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(htmlBody, "text/html; charset=utf-8");

        Transport.send(message);
    }
}