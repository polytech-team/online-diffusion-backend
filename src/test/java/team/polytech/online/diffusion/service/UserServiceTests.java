package team.polytech.online.diffusion.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import team.polytech.online.diffusion.entity.ImageEntity;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.GalleryPagingWrapper;
import team.polytech.online.diffusion.model.ProfileInfo;
import team.polytech.online.diffusion.repository.ImageRepository;
import team.polytech.online.diffusion.repository.UserRepository;
import team.polytech.online.diffusion.service.user.UserService;
import team.polytech.online.diffusion.utils.TestMockUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTests {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ImageRepository imageRepository;

    @Autowired
    private UserService userService;


    @Test
    public void UserService_getUserById_returnsUser() {
        TestMockUtils.mockStubUserById(userRepository);

        Optional<User> user = userService.getUserById(1L);

        Assertions.assertThat(user.isEmpty()).isFalse();
    }

    @Test
    public void UserService_changeUserPassword_changesPassword() {
        AtomicReference<String> password = new AtomicReference<>("base");

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenAnswer(var -> {
            User user = new User();
            user.setPassword(password.get());
            return Optional.of(user);
        });

        Mockito.when(userRepository.findById(Mockito.any())).thenAnswer(var -> {
            User user = new User();
            user.setPassword(password.get());
            return Optional.of(user);
        });

        Mockito.when(userRepository.save(Mockito.any())).thenAnswer(var -> {
            password.set("changed");
            return userRepository.findByUsername("").get();
        });


        Optional<User> user = userService.getUserById(1L);

        boolean success = userService.changeUserPassword("foo", "foo");

        Optional<User> newUser = userService.getUserById(1L);

        Assertions.assertThat(user.get().getPassword()).isEqualTo("base");
        Assertions.assertThat(success).isTrue();
        Assertions.assertThat(newUser.get().getPassword()).isEqualTo("changed");
    }

    @Test
    public void UserService_changeUserPassword_noUserFound() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThat(userService.changeUserPassword("foo", "boo"))
                .isFalse();
    }

    @Test
    public void UserService_changeUsername_changesUsername() {
        AtomicReference<String> username = new AtomicReference<>("base");

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenAnswer(var -> {
            User user = new User();
            user.setUsername(username.get());
            return Optional.of(user);
        });

        Mockito.when(userRepository.findById(Mockito.any())).thenAnswer(var -> {
            User user = new User();
            user.setUsername(username.get());
            return Optional.of(user);
        });

        Mockito.when(userRepository.save(Mockito.any())).thenAnswer(var -> {
            username.set("changed");
            return userRepository.findByUsername("").get();
        });

        Optional<User> user = userService.getUserById(1L);

        boolean success = userService.changeUsername("foo", "foo");

        Optional<User> newUser = userService.getUserById(1L);

        Assertions.assertThat(user.get().getUsername()).isEqualTo("base");
        Assertions.assertThat(success).isTrue();
        Assertions.assertThat(newUser.get().getUsername()).isEqualTo("changed");
    }

    @Test
    public void UserService_changeUsername_noUserFound() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThat(userService.changeUsername("foo", "boo"))
                .isFalse();
    }

    @Test
    public void UserService_changeUsername_alreadyExists() {
        TestMockUtils.mockStubUserByUsername(userRepository);
        Mockito.when(userRepository.existsByUsername(Mockito.any())).thenReturn(true);

        Assertions.assertThat(userService.changeUsername("foo", "boo"))
                .isFalse();
    }

    @Test
    public void UserService_getProfileInfo_returnsProfileInfo() {
        TestMockUtils.mockStubUserByUsername(userRepository);

        Optional<ProfileInfo> info = userService.getProfileInfo("foo");

        Assertions.assertThat(info.isPresent()).isTrue();
    }

    @Test
    public void UserService_getProfileInfo_noUserFound() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThat(userService.getProfileInfo("foo").isEmpty())
                .isTrue();
    }

    @Test
    public void UserService_getGallery_returnsGalleryPagingWrapper() {
        TestMockUtils.mockAuthInfo(TestMockUtils.mockStubUserByUsername(userRepository));

        Mockito.when(imageRepository.findAllByUserAndPublicityNot(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenAnswer(var -> {
                    ImageEntity entity = new ImageEntity();
                    entity.setUser(new User());
                    return new PageImpl<>(List.of(entity));
                });

        GalleryPagingWrapper wrapper = userService.getGallery(Optional.empty());

        Assertions.assertThat(wrapper).isNotNull();
        Assertions.assertThat(wrapper.getImages().size()).isEqualTo(1);
    }

    @Test
    public void UserService_getGallery_noUserFound() {
        TestMockUtils.mockAuthInfo(TestMockUtils.getStubUser());
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThat(userService.getGallery(Optional.empty()))
                .isNull();
    }

    @Test
    public void UserService_setAvatar_returnsGalleryPagingWrapper() {
        TestMockUtils.mockStubUserByUsername(userRepository);

        Mockito.when(imageRepository.findById(Mockito.any())).thenAnswer(var -> {
            ImageEntity entity = new ImageEntity();
            entity.setUser(new User());
            entity.setPublicity(ImageEntity.Publicity.PUBLIC);
            return Optional.of(entity);
        });

        boolean result = userService.setAvatar("foo", 1L);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void UserService_setAvatar_noUserFound() {
        TestMockUtils.mockAuthInfo(TestMockUtils.getStubUser());
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThat(userService.setAvatar("foo", 123L))
                .isFalse();
    }

    @Test
    public void UserService_setAvatar_noRightsToImage() {
        TestMockUtils.mockStubUserByUsername(userRepository);

        Mockito.when(imageRepository.findById(Mockito.any())).thenAnswer(var -> {
            ImageEntity entity = new ImageEntity();
            User user = TestMockUtils.getStubUser();
            user.setUsername("changed");
            entity.setUser(user);
            entity.setPublicity(ImageEntity.Publicity.PRIVATE);
            return Optional.of(entity);
        });

        Assertions.assertThat(userService.setAvatar("foo", 123L))
                .isFalse();
    }

}
