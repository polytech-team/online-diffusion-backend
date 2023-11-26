package team.polytech.online.diffusion.service.image;

import team.polytech.online.diffusion.model.Image;

import java.util.Optional;

public interface ImageService {

    Optional<Image> getImageById(long imageId);

}
