package team.polytech.online.diffusion.api;

import jakarta.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import team.polytech.online.diffusion.model.Image;
import team.polytech.online.diffusion.service.image.ImageService;
import team.polytech.online.diffusion.service.image.ImageServiceImpl;
import team.polytech.online.diffusion.service.user.UserService;

import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-10-26T02:19:33.552470+03:00[Europe/Moscow]")
@Controller
@RequestMapping("${openapi.onlineDiffusion.base-path:}")
public class ImagesApiController implements ImagesApi {

    private final NativeWebRequest request;
    private final ImageService service;

    private final UserService userService;

    @Autowired
    public ImagesApiController(NativeWebRequest request, ImageService service, UserService userService) {
        this.request = request;
        this.service = service;
        this.userService = userService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Image> getImage(Long photoId) {
        Optional<Image> image = service.getImageById(photoId);
        return image.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Void> makeAvatar(Long photoId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean setAvatar = userService.setAvatar(username,photoId);
        if (setAvatar) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Override
    public ResponseEntity<Void> putImage(Long photoId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ImageServiceImpl.PublishResult result = service.saveImageToGallery(username, photoId);

        switch (result) {
            case SUCCESS:
                return new ResponseEntity<>(HttpStatus.OK);
            case IMAGE_NOT_FOUND:
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            case USER_NOT_FOUND:
            case NOT_OWNED:
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            case ALREADY_IN_GALLERY:
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            default:
                return ResponseEntity.badRequest().build();
        }
    }
    @Override
    public ResponseEntity<Void> postImage( Long photoId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ImageServiceImpl.PublishResult result = service.publishImage(username, photoId);

        switch (result) {
            case SUCCESS:
                return ResponseEntity.status(HttpStatus.CREATED).build();
            case IMAGE_NOT_FOUND:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            case USER_NOT_FOUND:
            case NOT_OWNED:
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            case ALREADY_PUBLISHED:
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            default:
                return ResponseEntity.badRequest().build();
        }
    }
}
