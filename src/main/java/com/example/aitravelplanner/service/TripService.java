package com.example.aitravelplanner.service;

import com.example.aitravelplanner.dto.TripRequest;
import com.example.aitravelplanner.entity.Trip;
import com.example.aitravelplanner.entity.User;
import com.example.aitravelplanner.repository.TripRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public List<Trip> findTripsForUser(Long userId) {
        return tripRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Trip getTripForUser(Long tripId, Long userId) {
        return tripRepository.findByIdAndUserId(tripId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }

    @Transactional
    public Trip createTrip(TripRequest request, User user) {
        validateDates(request);
        Trip trip = new Trip();
        applyRequest(trip, request);
        trip.setUser(user);
        trip.setCreatedAt(LocalDateTime.now());
        trip.setStatus("PLANNED");
        return tripRepository.save(trip);
    }

    @Transactional
    public Trip updateTrip(Long tripId, Long userId, TripRequest request) {
        validateDates(request);
        Trip trip = getTripForUser(tripId, userId);
        applyRequest(trip, request);
        return tripRepository.save(trip);
    }

    @Transactional
    public void deleteTrip(Long tripId, Long userId) {
        Trip trip = getTripForUser(tripId, userId);
        tripRepository.delete(trip);
    }

    @Transactional
    public Trip updateStatus(Long tripId, Long userId, String status) {
        Trip trip = getTripForUser(tripId, userId);
        trip.setStatus(status);
        return tripRepository.save(trip);
    }

    private void validateDates(TripRequest request) {
        if (request.getStartDate() != null && request.getEndDate() != null && request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    private void applyRequest(Trip trip, TripRequest request) {
        trip.setDestination(request.getDestination());
        trip.setOriginCity(request.getOriginCity());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setTravelers(request.getTravelers());
        trip.setBudget(request.getBudget());
        trip.setCurrency(request.getCurrency() == null || request.getCurrency().isBlank() ? "MUR" : request.getCurrency().trim().toUpperCase());
        trip.setTravelStyle(request.getTravelStyle());
        trip.setInterests(request.getInterests());
        trip.setNotes(request.getNotes());
        trip.setHeroTagline(buildTagline(request));
        trip.setBestSeason("Sun season");
    }

    private String buildTagline(TripRequest request) {
        String destination = request.getDestination() == null ? "your destination" : request.getDestination();
        String style = request.getTravelStyle() == null ? "dream" : request.getTravelStyle().toLowerCase();
        return "A tropical " + style + " escape to " + destination;
    }
}
