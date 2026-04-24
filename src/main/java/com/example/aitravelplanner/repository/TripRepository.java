package com.example.aitravelplanner.repository;

import com.example.aitravelplanner.entity.Trip;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Trip> findByIdAndUserId(Long id, Long userId);
    long countByUserId(Long userId);
}