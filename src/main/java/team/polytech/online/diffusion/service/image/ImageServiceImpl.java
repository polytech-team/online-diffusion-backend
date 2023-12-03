package team.polytech.online.diffusion.service.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import team.polytech.online.diffusion.entity.ImageEntity;
import team.polytech.online.diffusion.model.Image;
import team.polytech.online.diffusion.model.Post;
import team.polytech.online.diffusion.model.PostPagingWrapper;
import team.polytech.online.diffusion.repository.ImageRepository;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${spring.paging.page-size}")
    private int pagingSize;
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

    @Override
    public PostPagingWrapper getFeed(Optional<Integer> marker) {
        int pageNumber = marker.orElse(0);

        Page<ImageEntity> page = imageRepository.findAllByPublicity(ImageEntity.Publicity.PUBLIC,
                PageRequest.of(pageNumber, pagingSize, Sort.by("createdOn").descending()));
        PostPagingWrapper wrapper = new PostPagingWrapper();
        wrapper.posts(page.get().map(entity -> (Post) transformFromEntity(entity)).toList());
        wrapper.nextMarker(++pageNumber < page.getTotalPages() ? pageNumber : null);

        return wrapper;
    }

    public static Image transformFromEntity(ImageEntity entity) {
        return new Image(entity.getId(),
                entity.getURL(),
                entity.getUser().getUsername(),
                entity.getUser().getAvatarUrl(),
                entity.getPrompt(),
                entity.getAntiPrompt(),
                entity.getSeed(),
                entity.getModel());
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
