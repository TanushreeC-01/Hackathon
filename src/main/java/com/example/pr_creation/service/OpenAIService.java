package com.example.pr_creation.service;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

//@Service
//public class OpenAIService {
//
//    @Value("${openai.api.key}")
//    private String apiKey;
//
//    @Value("${openai.model}")
//    private String model;
//
//    public String validatePRWithAI(String prompt) {
//        try (CloseableHttpClient client = HttpClients.createDefault()) {
//            HttpPost request = new HttpPost("https://api.openai.com/v1/chat/completions");
//            request.setHeader("Authorization", "Bearer " + apiKey);
//            request.setHeader("Content-Type", "application/json");
//
////            String payload = """
////            {
////              "model": "%s",
////              "messages": [
////                { "role": "system", "content": "You are a helpful code reviewer." },
////                { "role": "user", "content": "%s" }
////              ]
////            }
////            """.formatted(model, prompt);
//            String payload = """
//                    {
//                      "model": "%s",
//                      "messages": [
//                        { "role": "system", "content": "You are a helpful code reviewer." },
//                        { "role": "user", "content": "%s" }
//                      ],
//                      "temperature": 0.7,
//                      "max_tokens": 1000
//                    }
//                    """.formatted(model, prompt.replace("\"", "\\\""));
//
//
//            request.setEntity(new StringEntity(payload));
//            var response = client.execute(request);
//            var reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//            return reader.lines().reduce("", (a, b) -> a + b);
//        } catch (Exception e) {
//            return "AI validation failed: " + e.getMessage();
//        }
//    }
//}
@Service public class OpenAIService {

    @Value("${openai.api.key}")private String apiKey;

    @Value("${openai.model}")private String model;

    public String validatePRWithAI(String prompt) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost("https://api.openai.com/v1/chat/completions");
            request.setHeader("Authorization", "Bearer " + apiKey);
            request.setHeader("Content-Type", "application/json");

            String escapedPrompt = prompt
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");

            String payload = String.format("""
            {
              "model": "%s",
              "messages": [
                { "role": "system", "content": "You are a helpful code reviewer." },
                { "role": "user", "content": "%s" }
              ]
            }
            """, model, escapedPrompt);

            request.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));

            var response = client.execute(request);
            int statusCode = response.getCode();
//            if (statusCode != 200) {
//                return "OpenAI API error: HTTP " + statusCode;
//            }

            var reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }

            String rawJson = responseBuilder.toString();
            String marker = "\"content\":\"";
            int start = rawJson.indexOf(marker);
            if (start == -1) return "AI response not found.";

            start += marker.length();
            int end = rawJson.indexOf("\"", start);
            if (end == -1) return "AI response incomplete.";

            String content = rawJson.substring(start, end);
            return content.replace("\\n", "\n").replace("\\\"", "\"");

        } catch (Exception e) {
            return "AI validation failed: " + e.getMessage();
        }
    }}
