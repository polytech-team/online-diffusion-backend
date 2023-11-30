package team.polytech.online.diffusion.service.image;

import team.polytech.online.diffusion.model.Image;

import java.util.Optional;

public interface ImageService {

    Optional<Image> getImageById(long imageId);

    ImageServiceImpl.PublishResult saveImageToGallery(String username , Long photoId);

    ImageServiceImpl.PublishResult publishImage(String username, Long photoId);
}
