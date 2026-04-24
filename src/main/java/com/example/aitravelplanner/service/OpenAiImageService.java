package com.example.aitravelplanner.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OpenAiImageService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    @Value("${openai.image-model:gpt-image-1}")
    private String imageModel;

    @Value("${app.images.storage-dir:generated-images}")
    private String storageDir;

    public String buildPrompt(String destination, String travelStyle, String interests) {
        return "Create a premium tropical travel poster for " + destination
            + ", dream destination mood, vivid ocean blues, sunset warmth, luxury editorial travel photography, "
            + travelStyle + " style, featuring hints of " + (interests == null || interests.isBlank() ? "beaches and culture" : interests)
            + ". No text, no watermark.";
    }

    public String generateDestinationImage(String destination, String travelStyle, String interests) {
        String prompt = buildPrompt(destination, travelStyle, interests);
        if (apiKey == null || apiKey.isBlank()) {
            return ensureFallbackImage(destination, travelStyle, interests);
        }
        try {
            RestClient client = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

            String payload = objectMapper.writeValueAsString(java.util.Map.of(
                "model", imageModel,
                "prompt", prompt,
                "size", "1024x1024"
            ));

            String body = client.post()
                .uri("/images/generations")
                .body(payload)
                .retrieve()
                .body(String.class);

            JsonNode root = objectMapper.readTree(body);
            JsonNode data = root.path("data");
            if (data.isArray() && !data.isEmpty()) {
                JsonNode first = data.get(0);
                if (first.hasNonNull("url")) {
                    return first.get("url").asText();
                }
                if (first.hasNonNull("b64_json")) {
                    byte[] bytes = Base64.getDecoder().decode(first.get("b64_json").asText());
                    return saveBinary(bytes, sanitize(destination) + "-" + UUID.randomUUID() + ".png");
                }
            }
        } catch (Exception ignored) {
        }
        return ensureFallbackImage(destination, travelStyle, interests);
    }

    private String ensureFallbackImage(String destination, String travelStyle, String interests) {
        String safeName = sanitize(destination) + ".svg";
        Path dir = Paths.get(storageDir);
        Path file = dir.resolve(safeName);
        try {
            Files.createDirectories(dir);
            if (!Files.exists(file)) {
                Files.writeString(file, buildSvg(destination, travelStyle, interests), StandardCharsets.UTF_8);
            }
        } catch (IOException ignored) {
            return "/images/tropical-default.svg";
        }
        return "/generated-images/" + safeName;
    }

    private String saveBinary(byte[] bytes, String fileName) throws IOException {
        Path dir = Paths.get(storageDir);
        Files.createDirectories(dir);
        Path file = dir.resolve(fileName);
        Files.write(file, bytes);
        return "/generated-images/" + fileName;
    }

    private String sanitize(String value) {
        return value == null ? "dream-trip" : value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }

    private String buildSvg(String destination, String travelStyle, String interests) {
        String title = escape(destination == null ? "Dream Destination" : destination);
        String subtitle = escape((travelStyle == null ? "Tropical" : travelStyle) + " · " + (interests == null || interests.isBlank() ? "Lagoon & culture" : interests));
        return String.format("""
            <svg xmlns="http://www.w3.org/2000/svg" width="1200" height="800" viewBox="0 0 1200 800">
              <defs>
                <linearGradient id="sky" x1="0" y1="0" x2="1" y2="1">
                  <stop offset="0%%" stop-color="#1a6cff"/>
                  <stop offset="55%%" stop-color="#13c7c3"/>
                  <stop offset="100%%" stop-color="#ffd36f"/>
                </linearGradient>
                <linearGradient id="sea" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%%" stop-color="#00b9ff" stop-opacity="0.9"/>
                  <stop offset="100%%" stop-color="#025e86"/>
                </linearGradient>
              </defs>
              <rect width="1200" height="800" fill="url(#sky)"/>
              <circle cx="945" cy="156" r="96" fill="#fff1a8" fill-opacity="0.82"/>
              <path d="M0 470 C160 420 240 430 360 470 C520 520 640 500 760 470 C905 430 1040 432 1200 490 L1200 800 L0 800 Z" fill="url(#sea)"/>
              <path d="M0 560 C140 520 290 540 420 580 C560 625 700 620 860 580 C1020 540 1110 535 1200 565 L1200 800 L0 800 Z" fill="#0f8ea4" fill-opacity="0.55"/>
              <path d="M1040 590 C1030 510 965 472 910 510 C860 547 878 618 934 654 C982 683 1042 676 1084 650 C1148 611 1131 537 1080 515 C1062 507 1046 516 1040 590 Z" fill="#187c49" fill-opacity="0.82"/>
              <path d="M144 600 C214 530 276 486 332 468 C316 548 303 628 292 710 C246 694 193 661 144 600 Z" fill="#0e8549" fill-opacity="0.9"/>
              <path d="M252 480 C212 392 198 325 211 270 C280 318 330 370 362 423 C324 445 287 463 252 480 Z" fill="#13995b" fill-opacity="0.86"/>
              <path d="M356 426 C383 332 424 270 471 226 C495 298 496 372 476 448 C434 444 394 437 356 426 Z" fill="#0f8d54" fill-opacity="0.83"/>
              <rect x="88" y="74" rx="24" ry="24" width="620" height="142" fill="#03172d" fill-opacity="0.42"/>
              <text x="122" y="138" fill="#ffffff" font-family="Arial, Helvetica, sans-serif" font-size="68" font-weight="700">%s</text>
              <text x="124" y="184" fill="#e7f8ff" font-family="Arial, Helvetica, sans-serif" font-size="28">%s</text>
              <text x="894" y="720" fill="#ffffff" fill-opacity="0.92" font-family="Arial, Helvetica, sans-serif" font-size="26">AI Travel Planner</text>
            </svg>
            """, title, subtitle);
    }

    private String escape(String input) {
        return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
