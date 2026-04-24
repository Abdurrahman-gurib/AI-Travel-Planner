package com.example.aitravelplanner.controller;

import com.example.aitravelplanner.dto.TripRequest;
import com.example.aitravelplanner.entity.Trip;
import com.example.aitravelplanner.entity.User;
import com.example.aitravelplanner.service.AiPlannerService;
import com.example.aitravelplanner.service.TripService;
import com.example.aitravelplanner.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;
    private final AiPlannerService aiPlannerService;
    private final SecurityUtil securityUtil;

    public TripController(TripService tripService,
                          AiPlannerService aiPlannerService,
                          SecurityUtil securityUtil) {
        this.tripService = tripService;
        this.aiPlannerService = aiPlannerService;
        this.securityUtil = securityUtil;
    }

    @ModelAttribute("travelStyles")
    public List<String> travelStyles() {
        return List.of(
                "Budget",
                "Standard",
                "Luxury",
                "Adventure",
                "Family",
                "Business",
                "Island Romance",
                "Wellness"
        );
    }

    @ModelAttribute("tripStatuses")
    public List<String> tripStatuses() {
        return List.of("PLANNED", "UPCOMING", "COMPLETED", "CANCELLED");
    }

    @ModelAttribute("currencies")
    public List<String> currencies() {
        return List.of("MUR", "USD", "EUR", "GBP", "AED", "JPY");
    }

    @GetMapping
    public String listTrips(Model model) {
        User user = securityUtil.currentUser();
        model.addAttribute("trips", tripService.findTripsForUser(user.getId()));
        return "trips";
    }

    @GetMapping("/new")
    public String newTripForm(Model model) {
        TripRequest request = new TripRequest();
        request.setCurrency("MUR");
        request.setTravelStyle("Island Romance");
        request.setTravelers(2);

        model.addAttribute("tripRequest", request);
        model.addAttribute("pageTitle", "Create Dream Trip");
        model.addAttribute("formAction", "/trips");
        return "trip-form";
    }

    @PostMapping
    public String createTrip(@Valid @ModelAttribute("tripRequest") TripRequest tripRequest,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Create Dream Trip");
            model.addAttribute("formAction", "/trips");
            return "trip-form";
        }

        try {
            tripService.createTrip(tripRequest, securityUtil.currentUser());
            redirectAttributes.addFlashAttribute("successMessage", "Trip created successfully.");
            return "redirect:/trips?created";
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("date.invalid", ex.getMessage());
            model.addAttribute("pageTitle", "Create Dream Trip");
            model.addAttribute("formAction", "/trips");
            return "trip-form";
        }
    }

    @GetMapping("/{id}")
    public String viewTrip(@PathVariable Long id, Model model) {
        User user = securityUtil.currentUser();
        model.addAttribute("trip", tripService.getTripForUser(id, user.getId()));
        return "trip-details";
    }

    @GetMapping("/{id}/edit")
    public String editTrip(@PathVariable Long id, Model model) {
        User user = securityUtil.currentUser();
        Trip trip = tripService.getTripForUser(id, user.getId());

        TripRequest request = new TripRequest();
        request.setDestination(trip.getDestination());
        request.setOriginCity(trip.getOriginCity());
        request.setStartDate(trip.getStartDate());
        request.setEndDate(trip.getEndDate());
        request.setTravelers(trip.getTravelers());
        request.setBudget(trip.getBudget());
        request.setCurrency(trip.getCurrency());
        request.setTravelStyle(trip.getTravelStyle());
        request.setInterests(trip.getInterests());
        request.setNotes(trip.getNotes());

        model.addAttribute("tripRequest", request);
        model.addAttribute("trip", trip);
        model.addAttribute("pageTitle", "Edit Trip");
        model.addAttribute("formAction", "/trips/" + id + "/update");
        return "trip-form";
    }

    @PostMapping("/{id}/update")
    public String updateTrip(@PathVariable Long id,
                             @Valid @ModelAttribute("tripRequest") TripRequest tripRequest,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        User user = securityUtil.currentUser();

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Trip");
            model.addAttribute("formAction", "/trips/" + id + "/update");
            model.addAttribute("trip", tripService.getTripForUser(id, user.getId()));
            return "trip-form";
        }

        try {
            tripService.updateTrip(id, user.getId(), tripRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Trip updated successfully.");
            return "redirect:/trips/" + id + "?updated";
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("date.invalid", ex.getMessage());
            model.addAttribute("pageTitle", "Edit Trip");
            model.addAttribute("formAction", "/trips/" + id + "/update");
            model.addAttribute("trip", tripService.getTripForUser(id, user.getId()));
            return "trip-form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteTrip(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        User user = securityUtil.currentUser();
        tripService.deleteTrip(id, user.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Trip deleted successfully.");
        return "redirect:/trips?deleted";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               RedirectAttributes redirectAttributes) {
        User user = securityUtil.currentUser();
        tripService.updateStatus(id, user.getId(), status);
        redirectAttributes.addFlashAttribute("successMessage", "Trip status updated.");
        return "redirect:/trips/" + id + "?statusUpdated";
    }

    @PostMapping("/{id}/generate")
    public String generateTrip(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        User user = securityUtil.currentUser();
        Trip trip = tripService.getTripForUser(id, user.getId());

        aiPlannerService.generateForTrip(trip, user);

        redirectAttributes.addFlashAttribute("successMessage", "AI travel plan generated successfully.");
        return "redirect:/trips/" + id + "?generated";
    }
}