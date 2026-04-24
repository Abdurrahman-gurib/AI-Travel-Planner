package com.example.aitravelplanner.config;

import com.example.aitravelplanner.entity.Itinerary;
import com.example.aitravelplanner.entity.Trip;
import com.example.aitravelplanner.entity.User;
import com.example.aitravelplanner.repository.TripRepository;
import com.example.aitravelplanner.repository.UserRepository;
import com.example.aitravelplanner.service.OpenAiImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final PasswordEncoder passwordEncoder;
    private final OpenAiImageService imageService;

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;

    public DataSeeder(UserRepository userRepository,
                      TripRepository tripRepository,
                      PasswordEncoder passwordEncoder,
                      OpenAiImageService imageService) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

    @Override
    public void run(String... args) {
        if (!seedEnabled) {
            return;
        }

        User demo = userRepository.findByEmailIgnoreCase("demo@travelplanner.app")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFullName("Demo Traveler");
                    user.setEmail("demo@travelplanner.app");
                    user.setPassword(passwordEncoder.encode("password"));
                    user.setRole("USER");
                    user.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                });

        userRepository.findByEmailIgnoreCase("admin@travelplanner.app")
                .orElseGet(() -> {
                    User admin = new User();
                    admin.setFullName("Admin Planner");
                    admin.setEmail("admin@travelplanner.app");
                    admin.setPassword(passwordEncoder.encode("password"));
                    admin.setRole("ADMIN");
                    admin.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(admin);
                });

        // Development mode: clear demo user's trips so the saved trips page always shows the full demo set.
        tripRepository.deleteAll(tripRepository.findByUserIdOrderByCreatedAtDesc(demo.getId()));

        seedTrip(demo, "Bali, Indonesia", "Mauritius", 185000.0, "Island Romance",
                "beaches, spas, temples, sunset dining",
                "Oceanfront escape with beach clubs and spa time.",
                "UPCOMING", 15, 21,
                "https://images.unsplash.com/photo-1537996194471-e657df975ab4?auto=format&fit=crop&w=1200&q=80");

        seedTrip(demo, "Zanzibar, Tanzania", "Mauritius", 128000.0, "Wellness",
                "beaches, culture, spice tours, dhow cruise",
                "Soft tropical pace with sea breeze mornings and warm nights.",
                "UPCOMING", 32, 37,
                "https://images.unsplash.com/photo-1512100356356-de1b84283e18?auto=format&fit=crop&w=1200&q=80");

        seedTrip(demo, "Santorini, Greece", "Mauritius", 210000.0, "Luxury",
                "sunsets, boutique hotels, seafood, photography",
                "Blue-domed views, elegant evenings, and iconic caldera scenes.",
                "PLANNED", 45, 51,
                "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?auto=format&fit=crop&w=1200&q=80");

        seedTrip(demo, "Maldives", "Mauritius", 260000.0, "Luxury",
                "overwater villas, snorkeling, private dining, spa",
                "Crystal lagoons, slow luxury, and dreamy island calm.",
                "PLANNED", 55, 60,
                "https://images.unsplash.com/photo-1518684079-3c830dcef090?auto=format&fit=crop&w=1200&q=80");

        seedTrip(demo, "Dubai, UAE", "Mauritius", 165000.0, "Luxury",
                "shopping, skyline, desert safari, rooftop dining",
                "Luxury towers, polished malls, and dramatic desert evenings.",
                "PLANNED", 22, 27,
                "https://images.unsplash.com/photo-1512453979798-5ea266f8880c?auto=format&fit=crop&w=1200&q=80");

        seedTrip(demo, "Paris, France", "Mauritius", 198000.0, "Standard",
                "cafes, museums, romance, fashion",
                "Elegant city breaks, iconic architecture, and timeless charm.",
                "UPCOMING", 70, 77,
                "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=1200&q=80");

        seedTrip(demo, "Tokyo, Japan", "Mauritius", 240000.0, "Adventure",
                "food, culture, shopping, design",
                "Fast city rhythm, precise transport, and layered neighborhoods.",
                "COMPLETED", -60, -54,
                "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?auto=format&fit=crop&w=1200&q=80");

        seedTrip(demo, "Cape Town, South Africa", "Mauritius", 142000.0, "Family",
                "coastline, mountains, wine estates, family activities",
                "Scenic drives, coastlines, and layered day trips.",
                "UPCOMING", 26, 32,
                "https://images.unsplash.com/photo-1580060839134-75a5edca2e99?auto=format&fit=crop&w=1200&q=80");

        seedTrip(demo, "Mauritius North Coast Escape", "Curepipe", 42000.0, "Wellness",
                "lagoon views, beach walks, spa, resort dining",
                "A local luxury reset with turquoise views and soft island comfort.",
                "UPCOMING", 7, 10,
                "https://images.unsplash.com/photo-1500375592092-40eb2168fd21?auto=format&fit=crop&w=1200&q=80");

        seedTrip(demo, "Swiss Alps, Switzerland", "Mauritius", 310000.0, "Luxury",
                "mountains, scenic trains, alpine villages, wellness",
                "Snow peaks, clean air, luxury rail, and elegant slow travel.",
                "PLANNED", 130, 138,
                "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=1200&q=80");

        seedTrip(demo, "Rome, Italy", "Mauritius", 186000.0, "Standard",
                "history, pasta, piazzas, architecture",
                "Historic beauty, warm food culture, and iconic city atmosphere.",
                "PLANNED", 82, 88,
                "https://images.unsplash.com/photo-1521295121783-8a321d551ad2?auto=format&fit=crop&w=1200&q=80");

        seedTrip(demo, "New York, USA", "Mauritius", 275000.0, "Business",
                "skyline, shopping, Broadway, city life",
                "Fast, iconic, energetic, and packed with premium urban experiences.",
                "PLANNED", 95, 101,
                "https://images.unsplash.com/photo-1485871981521-5b1fd3805eee?auto=format&fit=crop&w=1200&q=80");
    }

    private void seedTrip(User user,
                          String destination,
                          String origin,
                          double budget,
                          String style,
                          String interests,
                          String notes,
                          String status,
                          int startOffset,
                          int endOffset,
                          String imageUrl) {

        Trip trip = new Trip();
        trip.setUser(user);
        trip.setDestination(destination);
        trip.setOriginCity(origin);
        trip.setStartDate(LocalDate.now().plusDays(startOffset));
        trip.setEndDate(LocalDate.now().plusDays(endOffset));
        trip.setTravelers(2);
        trip.setBudget(budget);
        trip.setCurrency("MUR");
        trip.setTravelStyle(style);
        trip.setInterests(interests);
        trip.setNotes(notes);
        trip.setStatus(status);
        trip.setHeroTagline("Dream escape to " + destination);
        trip.setBestSeason("Dry sunny months");
        trip.setCreatedAt(LocalDateTime.now().minusDays(Math.max(1, Math.abs(startOffset) / 2)));

        Itinerary itinerary = new Itinerary();
        itinerary.setTrip(trip);
        itinerary.setGeneratedBy("SEED");
        itinerary.setGeneratedAt(LocalDateTime.now().minusDays(2));
        itinerary.setHeadline("Curated " + style + " plan for " + destination);

        // Direct URLs: images display immediately without waiting for OpenAI generation.
        itinerary.setGeneratedImageUrl(imageUrl);
        itinerary.setImagePrompt(imageService.buildPrompt(destination, style, interests));

        itinerary.setItineraryText("""
                Day 1: Arrive, settle in, and enjoy a signature first impression with a scenic dinner or sunset experience.
                Day 2: Explore the most iconic highlights, take photographs, and enjoy a curated local meal.
                Day 3: Dive into culture, hidden gems, neighborhood charm, or beachside relaxation depending on the destination.
                Day 4: Enjoy a premium activity, shopping district, wellness moment, or flexible free time.
                Day 5: Keep the day soft and open for final discoveries and an organized departure.
                """);

        itinerary.setBudgetText(String.format("""
                - Flights and hotel shape the largest share of the budget
                - Food and cafés should be planned with room for one premium meal
                - Local transport can be kept efficient with a daily cap
                - Activities should focus on one or two signature highlights
                - Keep a reserve for upgrades and spontaneous moments
                - Total trip budget: %.2f MUR
                """, budget));

        itinerary.setInsightsText(destination + " suits a " + style + " experience very well. Focus on "
                + interests + ", keep one flexible half-day, and prioritize memorable signature moments over overpacking the schedule.");

        itinerary.setPackingListText("""
                - Passport and booking confirmations
                - Charger, adapter, phone, and power bank
                - Comfortable clothes and walking shoes
                - One elevated evening outfit
                - Sunglasses, sunscreen, toiletries, and medication
                """);

        itinerary.setChecklistText("""
                - Confirm passport validity
                - Recheck flights and hotel
                - Save transport and map apps
                - Set a daily MUR spending plan
                - Keep emergency contacts available
                - Share itinerary with someone you trust
                """);

        trip.setItinerary(itinerary);
        tripRepository.save(trip);
    }
}