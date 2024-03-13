package team.polytech.online.diffusion.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import team.polytech.online.diffusion.api.ImagesApi;
import team.polytech.online.diffusion.entity.ImageEntity;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.repository.ImageRepository;
import team.polytech.online.diffusion.repository.UserRepository;
import team.polytech.online.diffusion.utils.TestMockUtils;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Tag("IntegrationTest")
public class ImagesIntegrationTest {


    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ImagesApi imagesApiController;

    @BeforeEach
    public void mockAuthInfo() {
        TestMockUtils.mockAuthInfo();
    }

    @Test
    public void ImagesIntegrationTest_setAvatar_OK() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenAnswer(var -> {
            User user = new User();
            user.setUsername("Вася");
            return Optional.of(user);
        });
        Mockito.when(imageRepository.findById(Mockito.any())).thenAnswer(var -> {
            ImageEntity entity = new ImageEntity();
            User user = TestMockUtils.getStubUser();
            entity.setUser(user);
            entity.setPublicity(ImageEntity.Publicity.PUBLIC);
            return Optional.of(entity);
        });

        Assertions.assertThat(imagesApiController.makeAvatar(0L).
                getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ImagesIntegrationTest_setAvatar_noExistingUser_NOT_FOUND() {
        TestMockUtils.mockAuthInfo(TestMockUtils.getStubUser());
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThat(imagesApiController.makeAvatar(1L).
                getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void ImagesIntegrationTest_setAvatar_noExistingImage_NOT_FOUND() {
        Mockito.when(imageRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThat(imagesApiController.makeAvatar(1L).
                getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void ImagesIntegrationTest_setAvatar_noRights_NOT_FOUND() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenAnswer(var -> {
            User user = new User();
            return Optional.of(user);
        });
        Mockito.when(imageRepository.findById(Mockito.any())).thenAnswer(var -> {
            ImageEntity entity = new ImageEntity();
            User user = TestMockUtils.getStubUser();
            user.setUsername("changed");
            entity.setUser(user);
            entity.setPublicity(ImageEntity.Publicity.PRIVATE);
            return Optional.of(entity);
        });

        Assertions.assertThat(imagesApiController.makeAvatar(1L).
                getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void ImagesIntegrationTest_putImage_OK() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenAnswer(var -> {
            User user = new User();
            user.setUsername("Вася");
            return Optional.of(user);
        });
        Mockito.when(imageRepository.findById(Mockito.any())).thenAnswer(var -> {
            ImageEntity entity = new ImageEntity();
            User user = TestMockUtils.getStubUser();
            entity.setUser(user);
            entity.setPublicity(ImageEntity.Publicity.UNUSED);
            return Optional.of(entity);
        });


        ResponseEntity<Void> response = imagesApiController.putImage(1L);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ImagesIntegrationTest_putImage_CONFLICT() {

        Mockito.when(imageRepository.findById(Mockito.any())).thenAnswer(var -> {
            ImageEntity entity = new ImageEntity();
            User user = TestMockUtils.getStubUser();
            entity.setUser(user);
            entity.setPublicity(ImageEntity.Publicity.PUBLIC);
            return Optional.of(entity);
        });

        Assertions.assertThat(imagesApiController.putImage(1L).
                getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void ImagesIntegrationTest_putImage_FORBIDDEN() {
        Mockito.when(imageRepository.findById(Mockito.any())).thenAnswer(var -> {
            ImageEntity entity = new ImageEntity();
            User user = TestMockUtils.getStubUser();
            user.setUsername("changed");
            entity.setUser(user);
            entity.setPublicity(ImageEntity.Publicity.PUBLIC);
            return Optional.of(entity);
        });


        Assertions.assertThat(imagesApiController.putImage(1L).
                getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ImagesIntegrationTest_putImage_NOT_FOUND() {
        Mockito.when(imageRepository.findById(Mockito.any())).thenReturn(Optional.empty());


        Assertions.assertThat(imagesApiController.putImage(1L).
                getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void ImagesIntegrationTest_getImage_noImage_NOT_FOUND() {
        Mockito.when(imageRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThat(imagesApiController.getImage(1L).
                getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void ImagesIntegrationTest_getImage_noAccessRights_NOT_FOUND() {
        Mockito.when(imageRepository.findById(Mockito.any())).thenAnswer(var -> {
            ImageEntity entity = new ImageEntity();
            User user = TestMockUtils.getStubUser();
            user.setUsername("changed");
            entity.setUser(user);
            entity.setPublicity(ImageEntity.Publicity.PRIVATE);
            return Optional.of(entity);
        });
        Assertions.assertThat(imagesApiController.getImage(1L).
                getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void ImagesIntegrationTest_getImage_OK() {
        Mockito.when(imageRepository.findById(Mockito.any())).thenAnswer(var -> {
            ImageEntity entity = new ImageEntity();
            User user = TestMockUtils.getStubUser();
            entity.setUser(user);
            entity.setPublicity(ImageEntity.Publicity.PUBLIC);
            return Optional.of(entity);
        });
        Assertions.assertThat(imagesApiController.getImage(1L).
                getStatusCode()).isEqualTo(HttpStatus.OK);
    }


}
