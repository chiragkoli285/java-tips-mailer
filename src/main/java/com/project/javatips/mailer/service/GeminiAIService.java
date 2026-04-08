package com.project.javatips.mailer.service;

import com.google.gson.*;
import com.project.javatips.mailer.config.AppConfig;
import com.project.javatips.mailer.model.JavaTip;
import okhttp3.*;

import java.util.ArrayList;
import java.util.List;

public class GeminiAIService {

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/" +
                    "gemini-1.5-flash:generateContent";

    private final AppConfig config;
    private final OkHttpClient httpClient;
    private final Gson gson;

    public GeminiAIService(AppConfig config) {
        this.config = config;
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
    }

    public List<JavaTip> fetchDailyTips() throws Exception {
        String prompt = buildPrompt();
        String responseJson = callGeminiAPI(prompt);
        return parseResponse(responseJson);
    }

    private String buildPrompt() {
        return """
            You are a Java expert. Generate exactly 3 Java tips for a developer 
            with 3+ years of experience. Focus on practical, production-relevant topics.
            
            Topics can include: Java internals, Spring Boot, JVM tuning, design patterns,
            concurrency, performance optimization, Java 17+ features, best practices, Kafka,
            microservices patterns, or commonly asked interview topics.
            
            Respond ONLY with a valid JSON array. No extra text, no markdown, no backticks.
            Use this exact format:
            [
              {
                "topicTitle": "Topic name here",
                "briefExplanation": "4-5 sentence explanation",
                "codeExample": "short Java code example as a single string with \\n for newlines",
                "proTip": "one practical pro tip"
              }
            ]
            """;
    }

    private String callGeminiAPI(String prompt) throws Exception {
        String url = GEMINI_URL + "?key=" + config.getGeminiApiKey();

        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);

        JsonArray parts = new JsonArray();
        parts.add(textPart);

        JsonObject content = new JsonObject();
        content.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject requestBody = new JsonObject();
        requestBody.add("contents", contents);

        RequestBody body = RequestBody.create(
                gson.toJson(requestBody),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Gemini API failed: " + response.code()
                        + " " + response.body().string());
            }
            return response.body().string();
        }
    }

    private List<JavaTip> parseResponse(String responseJson) {
        List<JavaTip> tips = new ArrayList<>();
        try {
            JsonObject root = JsonParser.parseString(responseJson).getAsJsonObject();
            String text = root
                    .getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();

            // Clean up response just in case
            text = text.trim()
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            JsonArray tipsArray = JsonParser.parseString(text).getAsJsonArray();
            for (JsonElement element : tipsArray) {
                JsonObject obj = element.getAsJsonObject();
                JavaTip tip = new JavaTip(
                        obj.get("topicTitle").getAsString(),
                        obj.get("briefExplanation").getAsString(),
                        obj.get("codeExample").getAsString(),
                        obj.get("proTip").getAsString()
                );
                tips.add(tip);
            }
        } catch (Exception e) {
            System.err.println("Failed to parse Gemini response: " + e.getMessage());
        }
        return tips;
    }
}