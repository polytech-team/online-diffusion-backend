package team.polytech.online.diffusion.service.auth;

import team.polytech.online.diffusion.entity.RecoveryToken;
import team.polytech.online.diffusion.model.AuthInfo;
import team.polytech.online.diffusion.service.auth.response.RegistrationResponse;

import java.util.Optional;

public interface AuthService {
    AuthInfo login(String email, String password);
    boolean confirmRegistration(String token);
    RegistrationResponse register(String email, String username, String password);
    AuthInfo refresh(String refreshToken);
    String recovery(String email);
    Optional<RecoveryToken> confirmation(String token, Integer code);
    AuthServiceImpl.RecoveryResponse setNewPassword(String token, String password);
}
