package com.example.aitravelplanner.repository;

import com.example.aitravelplanner.entity.Itinerary;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    Optional<Itinerary> findByTripId(Long tripId);
}
