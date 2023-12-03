package team.polytech.online.diffusion.api;

import jakarta.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import team.polytech.online.diffusion.model.GalleryPagingWrapper;
import team.polytech.online.diffusion.model.ProfileInfo;
import team.polytech.online.diffusion.service.user.UserService;

import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-10-26T02:19:33.552470+03:00[Europe/Moscow]")
@Controller
@RequestMapping("${openapi.onlineDiffusion.base-path:}")
public class UserApiController implements UserApi {

    private final NativeWebRequest request;
    private final UserService userService;

    @Autowired
    public UserApiController(NativeWebRequest request, UserService userService) {
        this.request = request;
        this.userService = userService;

    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }


    @Override
    public ResponseEntity<Void> profilePassword(String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean passwordChanged = userService.changeUserPassword(username, newPassword);
        if (passwordChanged) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> profileUsername(String newUsername) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean usernameChanged = userService.changeUsername(username, newUsername);
        if (usernameChanged) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<ProfileInfo> getProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<ProfileInfo> profileInfo = userService.getProfileInfo(username);
        return profileInfo.map(info -> new ResponseEntity<>(info, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<GalleryPagingWrapper> profileGallery(Optional<Integer> marker) {
        GalleryPagingWrapper wrapper = userService.getGallery(marker);
        if (wrapper == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }
}
