package com.example.aitravelplanner.controller;

import com.example.aitravelplanner.entity.User;
import com.example.aitravelplanner.service.DashboardService;
import com.example.aitravelplanner.service.TripService;
import com.example.aitravelplanner.util.SecurityUtil;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final SecurityUtil securityUtil;
    private final TripService tripService;
    private final DashboardService dashboardService;

    public HomeController(SecurityUtil securityUtil, TripService tripService, DashboardService dashboardService) {
        this.securityUtil = securityUtil;
        this.tripService = tripService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("heroStats", Map.of(
            "itineraries", "AI itinerary generation",
            "budgeting", "Budget planning in MUR",
            "insights", "Dream-destination insights"
        ));
        model.addAttribute("dreamDestinations", List.of(
            Map.of("title", "Bali Escape", "copy", "Lagoon mornings, cliffside sunsets, and soft adventure days.", "image", "/images/bali-card.svg"),
            Map.of("title", "Santorini Glow", "copy", "Whitewashed luxury, blue domes, and golden hour dinners.", "image", "/images/santorini-card.svg"),
            Map.of("title", "Zanzibar Breeze", "copy", "Warm turquoise water, spice trails, and barefoot beach calm.", "image", "/images/zanzibar-card.svg")
        ));
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User user = securityUtil.currentUser();
        var trips = tripService.findTripsForUser(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("recentTrips", trips.stream().limit(6).toList());
        model.addAttribute("metrics", dashboardService.buildMetrics(trips));
        return "dashboard";
    }
}
