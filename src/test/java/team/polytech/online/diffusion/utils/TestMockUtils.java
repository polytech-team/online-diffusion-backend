package team.polytech.online.diffusion.utils;

import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import team.polytech.online.diffusion.entity.ImageEntity;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

public class TestMockUtils {

    public static final String TEST_EMAIL = "foo@gmail.com";
    public static final String TEST_PASSWORD = "sdlkfjjdklsg";

    public static User mockStubUserByUsername(UserRepository mock) {
        User user = getStubUser();
        Mockito.when(mock.findByUsername(Mockito.any())).thenAnswer(var -> Optional.of(user));
        return user;
    }

    public static User mockStubUserById(UserRepository mock) {
        User user = getStubUser();
        Mockito.when(mock.findById(Mockito.any())).thenAnswer(var -> Optional.of(user));
        return user;
    }

    public static void mockAuthInfo() {
        mockAuthInfo(getStubUser());
    }

    public static void mockAuthInfo(User user) {
        SecurityContextHolder.getContext().setAuthentication(getAuthInfo(user));
    }

    public static Authentication getAuthInfo(User user) {
        return new UsernamePasswordAuthenticationToken(
            user,
            null,
            null
        );
    }

    public static Authentication getStubAuthInfo() {
        return getAuthInfo(getStubUser());
    }

    public static User getStubUser() {
        User user = new User();
        user.setId(0L);
        user.setUsername("Вася");
        user.setPassword(TEST_PASSWORD);
        user.setEmail(TEST_EMAIL);
        user.setStatus(User.Status.CONFIRMED);
        user.setAvatarUrl("too");
        user.setGalleryImages(3L);
        user.setPosted(3L);
        return user;
    }

    public static ImageEntity getStubImageEntity() {
        ImageEntity entity = new ImageEntity();

        entity.setId(0L);
        entity.setPublicity(ImageEntity.Publicity.PUBLIC);
        entity.setUser(getStubUser());
        entity.setAntiPrompt("foo");
        entity.setPrompt("foo");
        entity.setModel("foo");
        entity.setSeed(1);
        entity.setURL("localhost");
        entity.setCreatedOn(Instant.now());
        return entity;
    }
}
