package team.polytech.online.diffusion.service.auth;

import team.polytech.online.diffusion.model.AuthInfo;

public interface AuthService {
    AuthInfo login(String email, String password);
    boolean confirmRegistration(String token);
    String register(String email, String username, String password);
    AuthInfo refresh(String refreshToken);
    String recovery(String email);
    AuthServiceImpl.RecoveryResponse confirmation(String token, Integer code);
    AuthServiceImpl.RecoveryResponse setNewPassword(String token, String password);
}
