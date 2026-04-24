package com.example.aitravelplanner.util;

import com.example.aitravelplanner.entity.User;
import com.example.aitravelplanner.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    private final UserRepository userRepository;

    public SecurityUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new IllegalStateException("No authenticated user");
        }
        return userRepository.findByEmailIgnoreCase(auth.getName())
            .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }
}
