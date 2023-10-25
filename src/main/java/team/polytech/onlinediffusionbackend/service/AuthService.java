package team.polytech.onlinediffusionbackend.service;

import team.polytech.onlinediffusionbackend.model.User;

public interface AuthService {
    TokenPair login(String username, String password);
    User register(String username, String password);
    TokenPair refresh(String refreshToken);
}
