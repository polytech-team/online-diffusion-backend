package team.polytech.online.diffusion.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.polytech.online.diffusion.entity.ImageEntity;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.ProfileInfo;
import team.polytech.online.diffusion.repository.ImageRepository;
import team.polytech.online.diffusion.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final ImageRepository imageRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageRepository = imageRepository;
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
        if (userRepository.existsByUsername(newUsername)) {
            return false;
        }
        User user = userOptional.get();
        user.setUsername(newUsername);
        userRepository.save(user);
        return true;
    }

    @Override
    public Optional<ProfileInfo> getProfileInfo(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        User user = userOptional.get();
        return Optional.of(createProfileInfo(user));
    }

    private ProfileInfo createProfileInfo(User user) {
        return new ProfileInfo(user.getUsername(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getGenerated(),
                user.getGalleryImages(),
                user.getPosted());
    }

    @Override
    public boolean setAvatar(String username, Long photoId) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        Optional<ImageEntity> imageOptional = imageRepository.findById(photoId);
        if (userOptional.isEmpty() || imageOptional.isEmpty()) {
            return false;
        }
        ImageEntity entity = imageOptional.get();
        if (entity.getPublicity() != ImageEntity.Publicity.PUBLIC && !entity.getUser().getUsername().equals(username)) {
            return false;
        }
        User user = userOptional.get();
        user.setAvatarUrl(imageOptional.get().getURL());
        userRepository.save(user);
        return true;
    }
}
