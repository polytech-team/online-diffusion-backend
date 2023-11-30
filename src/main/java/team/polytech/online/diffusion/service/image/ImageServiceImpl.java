package team.polytech.online.diffusion.service.image;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import team.polytech.online.diffusion.entity.ImageEntity;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.Image;
import team.polytech.online.diffusion.repository.ImageRepository;
import team.polytech.online.diffusion.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private final UserRepository userRepository;

    public ImageServiceImpl(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Image> getImageById(long imageId) {
        Optional<ImageEntity> entityOptional = imageRepository.findById(imageId);

        if (entityOptional.isEmpty()) {
            return Optional.empty();
        }

        ImageEntity entity = entityOptional.get();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (entity.getPublicity() != ImageEntity.Publicity.PUBLIC && !entity.getUser().getUsername().equals(username)) {
            return Optional.empty();
        }

        return Optional.of(transformFromEntity(entity));
    }

    private Image transformFromEntity(ImageEntity entity) {
        return new Image(entity.getId(),
                entity.getURL(),
                entity.getUser().getUsername(),
                null,
                entity.getPrompt(),
                entity.getAntiPrompt(),
                entity.getSeed(),
                entity.getModel());
    }

    @Override
    public PublishResult saveImageToGallery(String username, Long photoId) {
        Optional<ImageEntity> imageOpt = imageRepository.findById(photoId);

        if (!imageOpt.isPresent()) {
            return PublishResult.IMAGE_NOT_FOUND;
        }

        ImageEntity image = imageOpt.get();

        if (!image.getUser().getUsername().equals(username)) {
            return PublishResult.NOT_OWNED;
        }

        if (image.getPublicity() == ImageEntity.Publicity.PRIVATE) {
            return PublishResult.ALREADY_IN_GALLERY;
        }

        image.setPublicity(ImageEntity.Publicity.PRIVATE);
        imageRepository.save(image);
        return PublishResult.SUCCESS;
    }


    @Override
    public PublishResult publishImage(String username, Long photoId) {
        Optional<ImageEntity> imageOpt = imageRepository.findById(photoId);

        if (!imageOpt.isPresent()) {
            return PublishResult.IMAGE_NOT_FOUND;
        }

        ImageEntity image = imageOpt.get();

        if (!image.getUser().getUsername().equals(username)) {
            return PublishResult.NOT_OWNED;
        }

        if (image.getPublicity() == ImageEntity.Publicity.PUBLIC) {
            return PublishResult.ALREADY_PUBLISHED;
        }

        image.setPublicity(ImageEntity.Publicity.PUBLIC);
        imageRepository.save(image);
        return PublishResult.SUCCESS;
    }

    public enum PublishResult {
        SUCCESS,
        IMAGE_NOT_FOUND,
        USER_NOT_FOUND,
        NOT_OWNED,
        ALREADY_IN_GALLERY,
        ALREADY_PUBLISHED
    }
}
