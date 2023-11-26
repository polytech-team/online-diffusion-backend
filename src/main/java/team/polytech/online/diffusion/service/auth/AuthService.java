package team.polytech.online.diffusion.service.auth;

import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.AuthInfo;

public interface AuthService {
    AuthInfo login(String email, String password);
    User register(String email, String username, String password);
    AuthInfo refresh(String refreshToken);
}
