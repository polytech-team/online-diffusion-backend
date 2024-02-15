package team.polytech.online.diffusion.utils;

import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.repository.UserRepository;

import java.util.Optional;

public class TestMockUtils {

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
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                null
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    public static User getStubUser() {
        User user = new User();
        user.setId(0L);
        user.setUsername("Вася");
        user.setPassword("sdlkfjjdklsg");
        user.setEmail("foo@gmail.com");
        user.setStatus(User.Status.CONFIRMED);
        user.setAvatarUrl("too");
        user.setGalleryImages(3L);
        user.setPosted(3L);
        return user;
    }
}
