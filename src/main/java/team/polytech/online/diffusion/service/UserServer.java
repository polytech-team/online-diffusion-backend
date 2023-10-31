package team.polytech.online.diffusion.service;

import team.polytech.online.diffusion.entity.User;

import java.util.Optional;

public interface UserServer {
    Optional<User> getUserById(Long id);
    boolean changeUserPassword(String username, String newPassword);
    boolean changeUsername(String username, String newUsername);
    boolean usernameIsUnique(String username);
}
