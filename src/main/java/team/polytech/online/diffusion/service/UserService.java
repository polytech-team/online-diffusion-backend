package team.polytech.online.diffusion.service;

import team.polytech.online.diffusion.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);

    boolean changeUserPassword(String username, String newPassword);

    boolean changeUsername(String username, String newUsername);

}
