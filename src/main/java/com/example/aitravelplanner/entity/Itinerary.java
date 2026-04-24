package com.example.aitravelplanner.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "itineraries")
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", unique = true)
    private Trip trip;

    @Column(name = "itinerary_text", columnDefinition = "TEXT")
    private String itineraryText;

    @Column(name = "budget_text", columnDefinition = "TEXT")
    private String budgetText;

    @Column(name = "insights_text", columnDefinition = "TEXT")
    private String insightsText;

    @Column(name = "packing_list_text", columnDefinition = "TEXT")
    private String packingListText;

    @Column(name = "checklist_text", columnDefinition = "TEXT")
    private String checklistText;

    @Column(name = "generated_image_url", columnDefinition = "TEXT")
    private String generatedImageUrl;

    @Column(name = "image_prompt", columnDefinition = "TEXT")
    private String imagePrompt;

    @Column(name = "headline", length = 200)
    private String headline;

    @Column(name = "generated_by", nullable = false)
    private String generatedBy = "MOCK";

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }
    public String getItineraryText() { return itineraryText; }
    public void setItineraryText(String itineraryText) { this.itineraryText = itineraryText; }
    public String getBudgetText() { return budgetText; }
    public void setBudgetText(String budgetText) { this.budgetText = budgetText; }
    public String getInsightsText() { return insightsText; }
    public void setInsightsText(String insightsText) { this.insightsText = insightsText; }
    public String getPackingListText() { return packingListText; }
    public void setPackingListText(String packingListText) { this.packingListText = packingListText; }
    public String getChecklistText() { return checklistText; }
    public void setChecklistText(String checklistText) { this.checklistText = checklistText; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public String getGeneratedImageUrl() { return generatedImageUrl; }
    public void setGeneratedImageUrl(String generatedImageUrl) { this.generatedImageUrl = generatedImageUrl; }
    public String getImagePrompt() { return imagePrompt; }
    public void setImagePrompt(String imagePrompt) { this.imagePrompt = imagePrompt; }
    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }
}
