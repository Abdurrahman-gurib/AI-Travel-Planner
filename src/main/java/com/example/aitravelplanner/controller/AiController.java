package com.example.aitravelplanner.controller;

import com.example.aitravelplanner.service.AiPlannerService;
import com.example.aitravelplanner.service.TripService;
import com.example.aitravelplanner.util.SecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ai")
public class AiController {

    private final AiPlannerService aiPlannerService;
    private final TripService tripService;
    private final SecurityUtil securityUtil;

    public AiController(AiPlannerService aiPlannerService, TripService tripService, SecurityUtil securityUtil) {
        this.aiPlannerService = aiPlannerService;
        this.tripService = tripService;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/trips/{id}/generate")
    public String generate(@PathVariable Long id) {
        var user = securityUtil.currentUser();
        var trip = tripService.getTripForUser(id, user.getId());
        aiPlannerService.generateForTrip(trip, user);
        return "redirect:/trips/" + id + "?generated";
    }

    @PostMapping("/trips/{id}/regenerate")
    public String regenerate(@PathVariable Long id) {
        var user = securityUtil.currentUser();
        var trip = tripService.getTripForUser(id, user.getId());
        aiPlannerService.generateForTrip(trip, user);
        return "redirect:/trips/" + id + "?regenerated";
    }
}
