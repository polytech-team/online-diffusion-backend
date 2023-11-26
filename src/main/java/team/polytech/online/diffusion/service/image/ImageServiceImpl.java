package team.polytech.online.diffusion.service.image;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import team.polytech.online.diffusion.entity.ImageEntity;
import team.polytech.online.diffusion.model.Image;
import team.polytech.online.diffusion.repository.ImageRepository;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
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
}
