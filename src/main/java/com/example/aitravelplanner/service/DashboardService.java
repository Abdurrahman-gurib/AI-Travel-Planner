package com.example.aitravelplanner.service;

import com.example.aitravelplanner.entity.Trip;
import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    public Map<String, Object> buildMetrics(List<Trip> trips) {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalTrips", trips.size());
        metrics.put("upcomingTrips", trips.stream().filter(t -> t.getStartDate() != null && !t.getStartDate().isBefore(LocalDate.now())).count());
        metrics.put("completedTrips", trips.stream().filter(t -> "COMPLETED".equalsIgnoreCase(t.getStatus())).count());
        metrics.put("plannedBudget", trips.stream().mapToDouble(t -> t.getBudget() == null ? 0 : t.getBudget()).sum());
        metrics.put("favoriteDestination", trips.stream()
            .filter(t -> t.getDestination() != null)
            .collect(java.util.stream.Collectors.groupingBy(Trip::getDestination, java.util.stream.Collectors.counting()))
            .entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("—"));
        metrics.put("topStyle", trips.stream()
            .filter(t -> t.getTravelStyle() != null)
            .collect(java.util.stream.Collectors.groupingBy(Trip::getTravelStyle, java.util.stream.Collectors.counting()))
            .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("—"));
        metrics.put("avgTripLength", trips.stream().mapToLong(Trip::getDurationDays).average().orElse(0));
        metrics.put("aiReadyTrips", trips.stream().filter(t -> t.getItinerary() != null).count());
        return metrics;
    }
}
