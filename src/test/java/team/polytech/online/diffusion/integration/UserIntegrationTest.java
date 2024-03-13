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
import team.polytech.online.diffusion.api.UserApi;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.repository.UserRepository;
import team.polytech.online.diffusion.utils.TestMockUtils;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Tag("IntegrationTest")
public class UserIntegrationTest {
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserApi userController;

    @BeforeEach
    public void mockAuthInfo() {
        TestMockUtils.mockAuthInfo();
    }

    @Test
    public void UserIntegrationTest_change_password_OK() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenAnswer(var -> {
            User user = new User();
            return Optional.of(user);
        });

        Mockito.when(userRepository.save(Mockito.any())).thenAnswer(var -> {
            return userRepository.findByUsername("").get();
        });

        Assertions.assertThat(userController.profilePassword("123").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void UserIntegrationTest_change_password_INVALID_USER() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertThat(userController.profileUsername("123").getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void UserIntegrationTest_change_username_OK() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenAnswer(var -> {
            User user = new User();
            return Optional.of(user);
        });

        Mockito.when(userRepository.save(Mockito.any())).thenAnswer(var -> {
            return userRepository.findByUsername("").get();
        });

        Mockito.when(userRepository.existsByUsername(Mockito.any())).thenAnswer(var -> {
            return false;
        });

        Assertions.assertThat(userController.profileUsername("123").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void UserIntegrationTest_change_username_ALREADY_EXIST() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenAnswer(var -> {
            User user = new User();
            return Optional.of(user);
        });

        Mockito.when(userRepository.existsByUsername(Mockito.any())).thenAnswer(var -> {
            return true;
        });

        Assertions.assertThat(userController.profileUsername("123").getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void UserIntegrationTest_change_username_INVALID_USER() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertThat(userController.profileUsername("123").getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
