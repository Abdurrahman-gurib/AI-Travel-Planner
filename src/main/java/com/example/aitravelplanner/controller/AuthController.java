package com.example.aitravelplanner.controller;

import com.example.aitravelplanner.dto.RegisterRequest;
import com.example.aitravelplanner.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                           BindingResult bindingResult,
                           Model model) {
        if (authService.emailExists(registerRequest.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "An account with this email already exists.");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("registerRequest", registerRequest);
            return "register";
        }

        authService.register(registerRequest);
        return "redirect:/login?registered";
    }
}
