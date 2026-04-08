package com.project.javatips.mailer.service;

import com.project.javatips.mailer.model.JavaTip;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmailFormatterService {

    public String buildHtmlEmail(List<JavaTip> tips) {
        StringBuilder html = new StringBuilder();
        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("EEEE, MMMM dd yyyy"));

        html.append("""
            <html><body style="font-family:Arial,sans-serif;
                max-width:700px;margin:auto;background:#f4f6f9;padding:20px;">
            <div style="background:#1F3864;padding:24px;border-radius:10px 10px 0 0;
                text-align:center;">
                <h1 style="color:white;margin:0;font-size:24px;">
                    ☕ Daily Java Tips
                </h1>
                <p style="color:#a8c4e0;margin:6px 0 0;">%s</p>
            </div>
            <div style="background:white;padding:24px;
                border-radius:0 0 10px 10px;box-shadow:0 2px 8px rgba(0,0,0,0.1);">
            """.formatted(date));

        int i = 1;
        for (JavaTip tip : tips) {
            html.append("""
                <div style="border-left:4px solid #2C5F8A;
                    padding:16px;margin-bottom:24px;background:#f8fafc;
                    border-radius:0 8px 8px 0;">
                    <h2 style="color:#1F3864;margin:0 0 8px;font-size:18px;">
                        Tip %d: %s
                    </h2>
                    <p style="color:#333;line-height:1.6;margin:0 0 12px;">%s</p>
                    <pre style="background:#1e1e2e;color:#cdd6f4;padding:14px;
                        border-radius:6px;overflow-x:auto;font-size:13px;
                        line-height:1.5;">%s</pre>
                    <div style="background:#e8f4fd;padding:10px 14px;
                        border-radius:6px;margin-top:10px;">
                        <strong style="color:#2C5F8A;">💡 Pro Tip:</strong>
                        <span style="color:#444;"> %s</span>
                    </div>
                </div>
                """.formatted(i++,
                    escapeHtml(tip.getTopicTitle()),
                    escapeHtml(tip.getBriefExplanation()),
                    escapeHtml(tip.getCodeExample()),
                    escapeHtml(tip.getProTip())));
        }

        html.append("""
                <p style="text-align:center;color:#888;font-size:12px;margin-top:24px;">
                    Keep learning, keep building 🚀<br>
                    <em>Your Daily Java Tips Bot</em>
                </p>
            </div></body></html>
            """);

        return html.toString();
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}