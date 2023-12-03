package team.polytech.online.diffusion.service.user;

import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.GalleryPagingWrapper;
import team.polytech.online.diffusion.model.ProfileInfo;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);

    boolean changeUserPassword(String username, String newPassword);

    boolean changeUsername(String username, String newUsername);

    Optional<ProfileInfo> getProfileInfo(String username);

    boolean setAvatar(String username, Long photoId);

    GalleryPagingWrapper getGallery(Optional<Integer> marker);
}
