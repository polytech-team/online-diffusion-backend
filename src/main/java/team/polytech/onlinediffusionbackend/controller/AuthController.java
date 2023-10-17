package team.polytech.onlinediffusionbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import team.polytech.onlinediffusionbackend.service.AuthService;
import team.polytech.onlinediffusionbackend.service.TokenPair;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public TokenPair login(@RequestParam String username, @RequestParam String password) {
        return authService.login(username, password);
    }

    @GetMapping("/register")
    public Object registration(@RequestParam String username, @RequestParam String password) {
        return authService.register(username, password);
    }

    @GetMapping("/refresh")
    public Object refresh(@RequestParam String refreshToken) {
        TokenPair pair = authService.refresh(refreshToken);
        if (pair == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return pair;
    }
}
