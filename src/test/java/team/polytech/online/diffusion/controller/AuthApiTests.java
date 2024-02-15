package team.polytech.online.diffusion.controller;

import java.util.Collections;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.thymeleaf.TemplateEngine;

import team.polytech.online.diffusion.api.AuthApiController;
import team.polytech.online.diffusion.entity.RecoveryToken;
import team.polytech.online.diffusion.model.AuthInfo;
import team.polytech.online.diffusion.service.auth.AuthService;
import team.polytech.online.diffusion.service.auth.AuthServiceImpl;
import team.polytech.online.diffusion.service.auth.response.RegistrationResponse;
import team.polytech.online.diffusion.utils.TestMockUtils;

@ExtendWith(MockitoExtension.class)
public class AuthApiTests {

    public static final AuthInfo AUTH_INFO = new AuthInfo("refreshToken", "accessToken");
    public static final String TEST_EMAIL = "user@mail.ru";
    public static final int TEST_RECOVERY_CODE = 3123;
    public static final String TEST_RECOVERY_TOKEN = "asdasdasdsa";
    public static final String TEST_PASSWORD = "veryStrongPassword";

    @Mock
    private AuthService authService;
    @Mock
    private NativeWebRequest request;
    @Mock
    private TemplateEngine htmlTemplateEngine;

    @InjectMocks
    private AuthApiController authApiController;

    @BeforeEach
    public void mockAuthInfo() {
        TestMockUtils.mockAuthInfo();
    }

    @Test
    public void AuthApiController_login_OK() {
        Mockito.when(authService.login(Mockito.any(), Mockito.any()))
            .thenReturn(AUTH_INFO);

        Assertions.assertThat(authApiController.login("test@gmail.com", "testPassword").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void AuthApiController_login_BAD_REQUEST() {
        Mockito.when(authService.login(Mockito.any(), Mockito.any()))
            .thenReturn(null);

        Assertions.assertThat(authApiController.login("test@gmail.com", "testPassword").getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void AuthApiController_emailConfirmation_OK() {
        Mockito.when(authService.confirmRegistration(Mockito.any()))
            .thenReturn(true);

        Assertions.assertThat(authApiController.emailConfirmation("123123").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void AuthApiController_emailConfirmation_NOT_FOUND() {
        Mockito.when(authService.confirmRegistration(Mockito.any()))
            .thenReturn(false);

        Assertions.assertThat(authApiController.emailConfirmation("123123").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void AuthApiController_refreshToken_OK() {
        Mockito.when(authService.refresh(Mockito.any()))
            .thenReturn(AUTH_INFO);

        Assertions.assertThat(authApiController.refreshToken("refreshToken1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void AuthApiController_register_OK() {
        Mockito.when(authService.register(Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(new RegistrationResponse("123", Collections.emptyList()));

        Assertions.assertThat(authApiController.register("user@gmail.com", "user", TEST_PASSWORD).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void AuthApiController_register_CONFLICT() {
        Mockito.when(authService.register(Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(new RegistrationResponse(null, Collections.emptyList()));

        Assertions.assertThat(authApiController.register("user@gmail.com", "user", TEST_PASSWORD).getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void AuthApiController_recovery_OK() {
        Mockito.when(authService.recovery(Mockito.any()))
            .thenReturn("1001");

        Assertions.assertThat(authApiController.recovery(TEST_EMAIL).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void AuthApiController_recovery_BAD_REQUEST() {
        Mockito.when(authService.recovery(Mockito.any()))
            .thenReturn(null);

        Assertions.assertThat(authApiController.recovery(TEST_EMAIL).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void AuthApiController_confirmation_READY() {
        final RecoveryToken recoveryToken = new RecoveryToken("123", 123L, 1, RecoveryToken.Stage.READY, 5);
        Mockito.when(authService.confirmation(Mockito.any(), Mockito.any()))
            .thenReturn(Optional.of(recoveryToken));

        Assertions.assertThat(authApiController.confirmation(TEST_RECOVERY_CODE, TEST_RECOVERY_TOKEN).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void AuthApiController_confirmation_NOT_CONFIRMED() {
        final RecoveryToken recoveryToken = new RecoveryToken("123", 123L, 1, RecoveryToken.Stage.NOT_CONFIRMED, 5);
        Mockito.when(authService.confirmation(Mockito.any(), Mockito.any()))
            .thenReturn(Optional.of(recoveryToken));

        Assertions.assertThat(authApiController.confirmation(TEST_RECOVERY_CODE, TEST_RECOVERY_TOKEN).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void AuthApiController_confirmation_USED() {
        final RecoveryToken recoveryToken = new RecoveryToken("123", 123L, 1, RecoveryToken.Stage.USED, 5);
        Mockito.when(authService.confirmation(Mockito.any(), Mockito.any()))
            .thenReturn(Optional.of(recoveryToken));

        Assertions.assertThat(authApiController.confirmation(TEST_RECOVERY_CODE, TEST_RECOVERY_TOKEN).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void AuthApiController_confirmation_empty() {
        Mockito.when(authService.confirmation(Mockito.any(), Mockito.any()))
            .thenReturn(Optional.empty());

        Assertions.assertThat(authApiController.confirmation(TEST_RECOVERY_CODE, TEST_RECOVERY_TOKEN).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void AuthApiController_newPassword_SUCCESS() {
        Mockito.when(authService.setNewPassword(Mockito.any(), Mockito.any()))
            .thenReturn(AuthServiceImpl.RecoveryResponse.SUCCESS);

        Assertions.assertThat(authApiController.newPassword(TEST_PASSWORD, TEST_RECOVERY_TOKEN).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void AuthApiController_newPassword_FAILURE() {
        Mockito.when(authService.setNewPassword(Mockito.any(), Mockito.any()))
            .thenReturn(AuthServiceImpl.RecoveryResponse.FAILURE);

        Assertions.assertThat(authApiController.newPassword(TEST_PASSWORD, TEST_RECOVERY_TOKEN).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void AuthApiController_newPassword_INVALID_TOKEN() {
        Mockito.when(authService.setNewPassword(Mockito.any(), Mockito.any()))
            .thenReturn(AuthServiceImpl.RecoveryResponse.INVALID_TOKEN);

        Assertions.assertThat(authApiController.newPassword(TEST_PASSWORD, TEST_RECOVERY_TOKEN).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
