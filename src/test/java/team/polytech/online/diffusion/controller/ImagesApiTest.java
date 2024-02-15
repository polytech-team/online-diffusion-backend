package team.polytech.online.diffusion.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import team.polytech.online.diffusion.api.ImagesApiController;

import team.polytech.online.diffusion.model.Image;
import team.polytech.online.diffusion.model.PostPagingWrapper;
import team.polytech.online.diffusion.repository.ImageRepository;
import team.polytech.online.diffusion.service.image.ImageService;
import team.polytech.online.diffusion.service.image.ImageServiceImpl;
import team.polytech.online.diffusion.service.user.UserService;
import team.polytech.online.diffusion.utils.TestMockUtils;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ImagesApiTest {

    @Mock
    private ImageService imageService;
    @Mock
    private NativeWebRequest request;
    @Mock
    private UserService userService;

    @InjectMocks
    private ImagesApiController controller;

    @BeforeEach
    public void mockAuthInfo() {
        TestMockUtils.mockAuthInfo();
    }

    @Test
    void ImagesApiController_getImage_OK() {
        Long photoId = 1L;
        Image mockImage = new Image();
        when(imageService.getImageById(photoId)).thenReturn(Optional.of(mockImage));

        var response = controller.getImage(photoId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void ImagesApiController_getImage_WhenNotFound() {
        Long photoId = 1L;
        when(imageService.getImageById(photoId)).thenReturn(Optional.empty());

        ResponseEntity<Image> response = controller.getImage(photoId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void ImagesApiController_getImage_InvalidId() {
        ResponseEntity<Image> response = controller.getImage(-1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void ImagesApiController_makeAvatar_OK() {
        Long photoId = 1L;
        when(userService.setAvatar(anyString(), anyLong())).thenReturn(true);

        ResponseEntity<Void> response = controller.makeAvatar(photoId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void ImagesApiController_makeAvatar_NotFound() {
        Long photoId = 1L;
        when(userService.setAvatar("Вася", photoId)).thenReturn(false);

        ResponseEntity<Void> response = controller.makeAvatar(photoId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void ImagesApiController_putImage_OK() {
        Long photoId = 1L;
        when(imageService.saveImageToGallery("Вася", photoId)).thenReturn(ImageServiceImpl.PublishResult.SUCCESS);

        ResponseEntity<Void> response = controller.putImage(photoId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void ImagesApiController_putImage_ImageNotFound() {
        Long photoId = 1L;
        when(imageService.saveImageToGallery("Вася", photoId)).thenReturn(ImageServiceImpl.PublishResult.IMAGE_NOT_FOUND);

        ResponseEntity<Void> response = controller.putImage(photoId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void ImagesApiController_postImage_OK() {
        Long photoId = 1L;
        when(imageService.publishImage("Вася", photoId)).thenReturn(ImageServiceImpl.PublishResult.SUCCESS);

        ResponseEntity<Void> response = controller.postImage(photoId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void ImagesApiController_postImage_UserNotFound() {
        Long photoId = 1L;
        when(imageService.publishImage("Вася", photoId)).thenReturn(ImageServiceImpl.PublishResult.USER_NOT_FOUND);

        ResponseEntity<Void> response = controller.postImage(photoId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void ImagesApiController_feed_WithMarker() {
        Optional<Integer> marker = Optional.of(10);
        PostPagingWrapper expectedWrapper = new PostPagingWrapper();
        when(imageService.getFeed(marker)).thenReturn(expectedWrapper);

        ResponseEntity<PostPagingWrapper> response = controller.feed(marker);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedWrapper);
    }
    @Test
    void ImagesApiController_feed_NoMarker() {
        PostPagingWrapper expectedWrapper = new PostPagingWrapper();
        when(imageService.getFeed(Optional.empty())).thenReturn(expectedWrapper);

        ResponseEntity<PostPagingWrapper> response = controller.feed(Optional.empty());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedWrapper);
    }

    @Test
    void ImagesApiController_putImage_CONFLICT() {
        Long photoId = 1L;

        when(imageService.saveImageToGallery(anyString(), eq(photoId))).thenReturn(ImageServiceImpl.PublishResult.ALREADY_IN_GALLERY);

        ResponseEntity<Void> response = controller.putImage(photoId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void ImagesApiController_getRequest_OK() {
        Assertions.assertThat(controller.getRequest()).isNotNull();
    }

}
