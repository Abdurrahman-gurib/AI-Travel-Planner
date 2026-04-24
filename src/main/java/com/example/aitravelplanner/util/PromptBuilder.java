package com.example.aitravelplanner.util;

import com.example.aitravelplanner.entity.Trip;
import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String buildTripPrompt(Trip trip) {
        return String.format(
            """
            You are a senior travel planner. Create a polished travel plan in clear markdown.
            Trip details:
            - Destination: %s
            - Origin city: %s
            - Dates: %s to %s
            - Duration: %d days
            - Travelers: %d
            - Budget: %.2f %s
            - Travel style: %s
            - Interests: %s
            - Notes: %s

            Output these sections exactly with these headings:
            ## ITINERARY
            Create a day-by-day plan with morning, afternoon, and evening suggestions when appropriate.

            ## BUDGET
            Provide a realistic budget breakdown with categories and estimated amounts in %s.

            ## INSIGHTS
            Give destination insights, safety advice, etiquette, weather/season notes, and local transport tips.

            ## PACKING LIST
            Group items into essentials, clothes, electronics, documents, and optional items.

            ## CHECKLIST
            Give a pre-departure checklist with actionable items.
            """,
            safe(trip.getDestination()),
            safe(trip.getOriginCity()),
            trip.getStartDate(),
            trip.getEndDate(),
            trip.getDurationDays(),
            trip.getTravelers(),
            trip.getBudget(),
            safe(trip.getCurrency()),
            safe(trip.getTravelStyle()),
            safe(trip.getInterests()),
            safe(trip.getNotes()),
            safe(trip.getCurrency())
        );
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "Not specified" : value;
    }
}
