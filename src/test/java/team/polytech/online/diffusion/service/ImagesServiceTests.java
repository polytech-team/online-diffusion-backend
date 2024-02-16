package team.polytech.online.diffusion.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import team.polytech.online.diffusion.entity.ImageEntity;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.Image;
import team.polytech.online.diffusion.model.PostPagingWrapper;
import team.polytech.online.diffusion.repository.ImageRepository;
import team.polytech.online.diffusion.service.image.ImageServiceImpl;
import team.polytech.online.diffusion.utils.TestMockUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ImagesServiceTests {

    @MockBean
    private ImageRepository imageRepository;


    @Autowired
    private ImageServiceImpl imageService;

    @BeforeEach
    public void mockAuthInfo() {
        TestMockUtils.mockAuthInfo();
    }

    @Test
    void imageService_getImageById_whenImagePublic() {
        ImageEntity publicImage =  createMockImageEntity(1L, "user", ImageEntity.Publicity.PUBLIC);
        when(imageRepository.findById(1L)).thenReturn(Optional.of(publicImage));

        Optional<Image> result = imageService.getImageById(1L);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getPhotoId()).isEqualTo(1L);
    }

    @Test
    void imageService_getImageById_whenPrivateImageNotOwned() {
        ImageEntity publicImage =  createMockImageEntity(1L, "userCustom", ImageEntity.Publicity.PRIVATE);
        when(imageRepository.findById(1L)).thenReturn(Optional.of(publicImage));

        Optional<Image> result = imageService.getImageById(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void imageService_getImageById_whenImageNotFound_ShouldReturnEmpty() {

        when(imageRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Image> result = imageService.getImageById(1L);

        assertThat(result).isEmpty();
    }


    @Test
    void imageService_publishImage_OK() {
        Long photoId = 1L;
        String username = "user";
        ImageEntity mockImage = new ImageEntity();
        mockImage.setId(photoId);
        mockImage.setPublicity(ImageEntity.Publicity.UNUSED);
        User mockUser = new User();
        mockUser.setUsername(username);
        mockImage.setUser(mockUser);

        when(imageRepository.findById(photoId)).thenReturn(Optional.of(mockImage));

        ImageServiceImpl.PublishResult result = imageService.publishImage(username, photoId);

        assertThat(result).isEqualTo(ImageServiceImpl.PublishResult.SUCCESS);
    }

    @Test
    void imageService_publishImage_NotFound() {
        Long photoId = 4L;
        String username = "user";
        when(imageRepository.findById(photoId)).thenReturn(Optional.empty());

        ImageServiceImpl.PublishResult result = imageService.publishImage(username, photoId);

        assertThat(result).isEqualTo(ImageServiceImpl.PublishResult.IMAGE_NOT_FOUND);
    }

    @Test
    void imageService_publishImage_NotOwned() {
        Long photoId = 6L;
        String ownerUsername = "ownerUser";
        String otherUsername = "otherUser";

        ImageEntity mockImage = createMockImageEntity(photoId, ownerUsername, ImageEntity.Publicity.UNUSED);

        when(imageRepository.findById(photoId)).thenReturn(Optional.of(mockImage));

        ImageServiceImpl.PublishResult result = imageService.publishImage(otherUsername, photoId);

        assertThat(result).isEqualTo(ImageServiceImpl.PublishResult.NOT_OWNED);
    }


    @Test
    void imageService_publishImage_AlreadyPublished() {
        Long photoId = 2L;
        String username = "user";
        ImageEntity mockImage = createMockImageEntity(photoId, username, ImageEntity.Publicity.PUBLIC);

        when(imageRepository.findById(photoId)).thenReturn(Optional.of(mockImage));

        ImageServiceImpl.PublishResult result = imageService.publishImage(username, photoId);

        assertThat(result).isEqualTo(ImageServiceImpl.PublishResult.ALREADY_PUBLISHED);
    }

    @Test
    void imageService_saveImageToGallery_Success() {
        Long photoId = 7L;
        String username = "user";
        ImageEntity mockImage = createMockImageEntity(photoId, username, ImageEntity.Publicity.UNUSED);

        when(imageRepository.findById(photoId)).thenReturn(Optional.of(mockImage));
        when(imageRepository.save(any(ImageEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ImageServiceImpl.PublishResult result = imageService.saveImageToGallery(username, photoId);

        assertThat(result).isEqualTo(ImageServiceImpl.PublishResult.SUCCESS);
        assertThat(mockImage.getPublicity()).isEqualTo(ImageEntity.Publicity.PRIVATE);
    }

    @Test
    void imageService_saveImageToGallery_NOT_OWNED() {

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(1L);
        User user = new User();
        user.setUsername("user");
        imageEntity.setUser(user);

        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(imageEntity));
        ImageServiceImpl.PublishResult result = imageService.saveImageToGallery("otherUser", 1L);
        assertEquals(ImageServiceImpl.PublishResult.NOT_OWNED, result);
    }

    @Test
    void imageService_saveImageToGallery_ALREADY_IN_GALLERY() {
        Long photoId = 1L;
        String username = "user";
        ImageEntity mockImage = new ImageEntity();
        mockImage.setId(photoId);
        mockImage.setPublicity(ImageEntity.Publicity.PRIVATE); // Assume the image is already in the gallery
        User mockUser = new User();
        mockUser.setUsername(username);
        mockImage.setUser(mockUser);

        when(imageRepository.findById(photoId)).thenReturn(Optional.of(mockImage));

        ImageServiceImpl.PublishResult result = imageService.saveImageToGallery(username, photoId);

        assertThat(result).isEqualTo(ImageServiceImpl.PublishResult.ALREADY_IN_GALLERY);
        SecurityContextHolder.clearContext();

    }


    @Test
    public void imageService_saveImageToGallery_IMAGE_NOT_FOUND() {

        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        ImageServiceImpl.PublishResult result = imageService.saveImageToGallery("username", 1L);

        assertThat(result).isEqualTo(ImageServiceImpl.PublishResult.IMAGE_NOT_FOUND);
    }


    @Test
    void imageService_getFeed_OK() {

        List<ImageEntity> mockImages = IntStream.range(0, 20)
                .mapToObj(i -> createMockImageEntity((long) i, "user" + i, ImageEntity.Publicity.PUBLIC))
                .collect(Collectors.toList());

        Page<ImageEntity> pageResponse = new PageImpl<>(mockImages);

        when(imageRepository.findAllByPublicity(eq(ImageEntity.Publicity.PUBLIC), any(PageRequest.class)))
                .thenReturn(pageResponse);

        PostPagingWrapper result = imageService.getFeed(Optional.of(0));

        assertThat(result.getPosts()).hasSize(20);
    }

    @Test
    void imageService_getFeed_ReturnsLimitedNumberOfImages() {
        int requestedPageSize = 5;
        List<ImageEntity> mockImages = IntStream.range(0, requestedPageSize)
                .mapToObj(i -> createMockImageEntity((long) i, "user" + i, ImageEntity.Publicity.PUBLIC))
                .collect(Collectors.toList());
        Page<ImageEntity> pageResponse = new PageImpl<>(mockImages, PageRequest.of(0, requestedPageSize), 100);

        when(imageRepository.findAllByPublicity(eq(ImageEntity.Publicity.PUBLIC), any(PageRequest.class)))
                .thenReturn(pageResponse);

        PostPagingWrapper result = imageService.getFeed(Optional.of(0));

        assertThat(result.getPosts()).hasSize(requestedPageSize);
    }

    @Test
    void imageService_getFeed_NoPublicImages() {
        when(imageRepository.findAllByPublicity(eq(ImageEntity.Publicity.PUBLIC), any(PageRequest.class)))
                .thenReturn(Page.empty());

        PostPagingWrapper result = imageService.getFeed(Optional.of(0));

        assertThat(result.getPosts()).isEmpty();
        assertThat(result.getNextMarker()).isNull();
    }

    private ImageEntity createMockImageEntity(Long id, String username, ImageEntity.Publicity publicity) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(id);
        User user = new User();
        user.setUsername(username);
        imageEntity.setUser(user);
        imageEntity.setPublicity(publicity);
        return imageEntity;
    }

}
