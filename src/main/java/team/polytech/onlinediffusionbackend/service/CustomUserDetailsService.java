package team.polytech.onlinediffusionbackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import team.polytech.onlinediffusionbackend.model.User;

public interface CustomUserDetailsService extends UserDetailsService {
    User getUserById(Long id);
}
