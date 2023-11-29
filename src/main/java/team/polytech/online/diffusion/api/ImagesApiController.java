package team.polytech.online.diffusion.api;

import jakarta.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import team.polytech.online.diffusion.model.Image;
import team.polytech.online.diffusion.service.image.ImageService;

import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-10-26T02:19:33.552470+03:00[Europe/Moscow]")
@Controller
@RequestMapping("${openapi.onlineDiffusion.base-path:}")
public class ImagesApiController implements ImagesApi {

    private final NativeWebRequest request;
    private final ImageService service;

    @Autowired
    public ImagesApiController(NativeWebRequest request, ImageService service) {
        this.request = request;
        this.service = service;
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
}
