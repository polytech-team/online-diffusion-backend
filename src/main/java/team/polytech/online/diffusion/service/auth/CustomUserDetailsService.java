package team.polytech.online.diffusion.service.auth;

import org.springframework.security.core.userdetails.UserDetailsService;

import team.polytech.online.diffusion.entity.User;

public interface CustomUserDetailsService extends UserDetailsService {
    User getUserById(Long id);
}
