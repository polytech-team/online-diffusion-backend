package team.polytech.online.diffusion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServerImpl implements UserServer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServerImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public boolean changeUserPassword(String username, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public boolean changeUsername(String username, String newUsername) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return false;
        }
        if (!usernameIsUnique(newUsername)) {
            return false;
        }
        User user = userOptional.get();
        user.setUsername(newUsername);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean usernameIsUnique(String username) {
        return !userRepository.findByUsername(username).isPresent();
    }
}
