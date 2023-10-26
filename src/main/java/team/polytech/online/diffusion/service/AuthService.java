package team.polytech.online.diffusion.service;

import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.AuthInfo;

public interface AuthService {
    AuthInfo login(String username, String password);
    User register(String username, String password);
    AuthInfo refresh(String refreshToken);
}
