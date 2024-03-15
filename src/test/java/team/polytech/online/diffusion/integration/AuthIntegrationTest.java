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
import org.springframework.security.authentication.AuthenticationManager;
import team.polytech.online.diffusion.api.AuthApi;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.AuthInfo;
import team.polytech.online.diffusion.model.InvalidData;
import team.polytech.online.diffusion.repository.AuthTokenRepository;
import team.polytech.online.diffusion.repository.RegistrationTokenRepository;
import team.polytech.online.diffusion.repository.UserRepository;
import team.polytech.online.diffusion.utils.TestMockUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Tag("IntegrationTest")
public class AuthIntegrationTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthTokenRepository authTokenRepository;
    @MockBean
    private RegistrationTokenRepository registrationRepository;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthApi authApiController;
    

    @BeforeEach
    public void mockAuthInfo() {
        TestMockUtils.mockAuthInfo();
    }

    @Test
    public void AuthIntegrationTest_login_OK() {
        when(authenticationManager.authenticate(any()))
            .thenReturn(TestMockUtils.getAuthInfo(TestMockUtils.getStubUser()));

        ResponseEntity<AuthInfo> response = authApiController.login("test@mail.ru", "testpassword");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void AuthIntegrationTest_login_BAD_REQUEST() {
        User user = TestMockUtils.getStubUser();
        user.setStatus(User.Status.NOT_CONFIRMED);
        when(authenticationManager.authenticate(any()))
            .thenReturn(TestMockUtils.getAuthInfo(user));

        ResponseEntity<AuthInfo> response = authApiController.login("test2@mail.ru", "strongtestpassword");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isNull();
    }

    @Test
    public void AuthIntegrationTest_register_OK() {
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        User user = new User();
        user.setId(1L);
        when(userRepository.save(Mockito.any())).thenReturn(user);

        ResponseEntity<List<String>> response = authApiController.register("user@gmail.com", "user", "strongpassword");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNull();
    }

    @Test
    public void AuthIntegrationTest_register_usernameAlreadyExists() {
        User user = new User();
        String username = "newUser";
        user.setUsername(username);
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(user));

        ResponseEntity<List<String>> response = authApiController.register("test@mail.ru", username, "password");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).containsExactly(InvalidData.USERNAME.getValue());
    }

    @Test
    public void AuthIntegrationTest_register_emailAlreadyExists() {
        User user = new User();
        user.setId(1L);
        String email = "newUser@mail.ru";
        user.setEmail(email);
        user.setStatus(User.Status.CONFIRMED);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));

        ResponseEntity<List<String>> response = authApiController.register(email, "user", "password");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).containsExactly(InvalidData.EMAIL.getValue());
    }
}
