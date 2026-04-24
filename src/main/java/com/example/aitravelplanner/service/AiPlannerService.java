package com.example.aitravelplanner.service;

import com.example.aitravelplanner.entity.AiLog;
import com.example.aitravelplanner.entity.Itinerary;
import com.example.aitravelplanner.entity.Trip;
import com.example.aitravelplanner.entity.User;
import com.example.aitravelplanner.repository.AiLogRepository;
import com.example.aitravelplanner.repository.ItineraryRepository;
import com.example.aitravelplanner.util.PromptBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
public class AiPlannerService {

    private final ItineraryRepository itineraryRepository;
    private final AiLogRepository aiLogRepository;
    private final PromptBuilder promptBuilder;
    private final OpenAiImageService openAiImageService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.model:gpt-4.1-mini}")
    private String model;

    @Value("${openai.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    public AiPlannerService(ItineraryRepository itineraryRepository,
                            AiLogRepository aiLogRepository,
                            PromptBuilder promptBuilder,
                            OpenAiImageService openAiImageService) {
        this.itineraryRepository = itineraryRepository;
        this.aiLogRepository = aiLogRepository;
        this.promptBuilder = promptBuilder;
        this.openAiImageService = openAiImageService;
    }

    @Transactional
    public Itinerary generateForTrip(Trip trip, User user) {
        String prompt = promptBuilder.buildTripPrompt(trip);
        String responseText;
        String provider;

        if (apiKey == null || apiKey.isBlank()) {
            responseText = buildMockResponse(trip);
            provider = "MOCK";
        } else {
            responseText = callOpenAi(prompt);
            provider = "OPENAI";
        }

        String imagePrompt = openAiImageService.buildPrompt(trip.getDestination(), trip.getTravelStyle(), trip.getInterests());
        String imageUrl = openAiImageService.generateDestinationImage(trip.getDestination(), trip.getTravelStyle(), trip.getInterests());

        AiLog log = new AiLog();
        log.setUser(user);
        log.setTrip(trip);
        log.setPrompt(prompt + "\n\n--- IMAGE PROMPT ---\n" + imagePrompt);
        log.setResponse(responseText + "\n\nIMAGE URL: " + imageUrl);

        log.setProviderUsed(provider);
        log.setCreatedAt(LocalDateTime.now());
        aiLogRepository.save(log);

        Itinerary itinerary = itineraryRepository.findByTripId(trip.getId()).orElseGet(Itinerary::new);
        itinerary.setTrip(trip);
        itinerary.setGeneratedBy(provider);
        itinerary.setGeneratedAt(LocalDateTime.now());
        itinerary.setItineraryText(extractSection(responseText, "ITINERARY"));
        itinerary.setBudgetText(extractSection(responseText, "BUDGET"));
        itinerary.setInsightsText(extractSection(responseText, "INSIGHTS"));
        itinerary.setPackingListText(extractSection(responseText, "PACKING LIST"));
        itinerary.setChecklistText(extractSection(responseText, "CHECKLIST"));
        itinerary.setGeneratedImageUrl(imageUrl);
        itinerary.setImagePrompt(imagePrompt);
        itinerary.setHeadline(extractHeadline(trip));
        trip.setItinerary(itinerary);
        return itineraryRepository.save(itinerary);
    }

    private String extractHeadline(Trip trip) {
        return "Your " + trip.getTravelStyle() + " escape to " + trip.getDestination();
    }

    private String callOpenAi(String prompt) {
        RestClient client = RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build();

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", model);
        payload.put("input", prompt);

        String body = client.post()
            .uri("/responses")
            .body(payload)
            .retrieve()
            .body(String.class);

        return parseResponseText(body);
    }

    private String parseResponseText(String body) {
        try {
            JsonNode root = objectMapper.readTree(body);
            if (root.hasNonNull("output_text")) {
                return root.get("output_text").asText();
            }
            JsonNode output = root.path("output");
            if (output.isArray() && !output.isEmpty()) {
                JsonNode first = output.get(0);
                JsonNode content = first.path("content");
                if (content.isArray() && !content.isEmpty()) {
                    JsonNode firstContent = content.get(0);
                    if (firstContent.hasNonNull("text")) {
                        return firstContent.get("text").asText();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return body;
    }

    private String extractSection(String content, String section) {
        String marker = "## " + section;
        int start = content.indexOf(marker);
        if (start < 0) {
            return "No " + section.toLowerCase() + " available yet.";
        }
        int from = start + marker.length();
        int next = content.indexOf("## ", from);
        if (next < 0) {
            next = content.length();
        }
        return content.substring(from, next).trim();
    }

    private String buildMockResponse(Trip trip) {
        String destination = trip.getDestination();
        String currency = trip.getCurrency() == null ? "MUR" : trip.getCurrency();
        long days = Math.max(1, trip.getDurationDays());
        double budgetPerDay = Math.max(1, trip.getBudget() / days);

        return String.format(
            """
            ## ITINERARY
            Day 1: Land in %s, settle into a dreamy hotel, enjoy a tropical welcome dinner, and unwind with a sunset stroll.
            Day 2: Signature highlights day with a balanced mix of iconic sights, relaxed photo stops, and one premium experience.
            Day 3: Culture and flavors day with markets, artisan spots, and a memorable lunch or dinner chosen around your interests.
            Day 4: Soft-adventure or beach day with wellness time, local transport tips, and a golden-hour viewpoint.
            Day 5: Flexible final day for shopping, hidden gems, and an easy departure rhythm.

            ## BUDGET
            - Accommodation: about %.0f %s per day
            - Food & cafés: about %.0f %s per day
            - Local transport: about %.0f %s per day
            - Activities: about %.0f %s per day
            - Buffer / emergency fund: about %.0f %s total
            Recommendation: keep 10-15%% of the total budget free for upgrades, weather changes, or spontaneous treats.

            ## INSIGHTS
            %s shines for a %s experience. Schedule early starts for high-demand attractions, keep one flexible block daily, use trusted ride or transit apps, and stay hydrated in warmer coastal zones. For a trip focused on %s, prioritize neighborhoods with strong local character and evening walkability.

            ## PACKING LIST
            Essentials: passport, cards, MUR backup cash, booking confirmations, insurance details.
            Clothes: breathable daytime outfits, one elevated evening look, swimwear if relevant, comfortable walking shoes, and a light layer.
            Electronics: phone, charger, power bank, camera, plug adapter, headphones.
            Documents: passport copy, itinerary, emergency contacts.
            Optional: reef-safe sunscreen, foldable beach bag, sunglasses, mini first-aid kit.

            ## CHECKLIST
            - Confirm passport validity and entry requirements
            - Recheck hotel and transfers
            - Set a daily budget cap near %.0f %s
            - Download offline maps and transport apps
            - Save emergency and embassy contacts
            - Share itinerary with one trusted contact
            """,
            destination,
            budgetPerDay * 0.35, currency,
            budgetPerDay * 0.22, currency,
            budgetPerDay * 0.10, currency,
            budgetPerDay * 0.18, currency,
            Math.max(5000, trip.getBudget() * 0.15), currency,
            destination,
            trip.getTravelStyle(),
            trip.getInterests() == null || trip.getInterests().isBlank() ? "beaches, culture, and dining" : trip.getInterests(),
            budgetPerDay, currency
        );
    }
}
