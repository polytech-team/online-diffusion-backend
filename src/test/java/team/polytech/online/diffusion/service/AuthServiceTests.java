package team.polytech.online.diffusion.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import team.polytech.online.diffusion.entity.RecoveryToken;
import team.polytech.online.diffusion.entity.RegistrationToken;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.InvalidData;
import team.polytech.online.diffusion.repository.AuthTokenRepository;
import team.polytech.online.diffusion.repository.RecoveryTokenRepository;
import team.polytech.online.diffusion.repository.RegistrationTokenRepository;
import team.polytech.online.diffusion.repository.UserRepository;
import team.polytech.online.diffusion.service.auth.AuthServiceImpl;
import team.polytech.online.diffusion.service.auth.CustomUserDetailsService;
import team.polytech.online.diffusion.service.auth.JwtService;
import team.polytech.online.diffusion.service.auth.response.RegistrationResponse;
import team.polytech.online.diffusion.utils.TestMockUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AuthServiceTests {
    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthTokenRepository authTokenRepository;

    @MockBean
    private RegistrationTokenRepository registrationTokenRepository;

    @MockBean
    private RecoveryTokenRepository recoveryTokenRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JavaMailSender mailSender;

    @BeforeEach
    public void mockAuthInfo() {
        TestMockUtils.mockAuthInfo(TestMockUtils.getStubUser());
    }

    @Test
    void authService_confirmRegistration_nullToken() {
        assertThat(authService.confirmRegistration(null)).isFalse();
    }

    @Test
    void authService_confirmRegistration_empty() {
        when(authenticationManager.authenticate(Mockito.any())).thenReturn(TestMockUtils.getStubAuthInfo());

        when(registrationTokenRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThat(authService.confirmRegistration("badToken")).isFalse();
    }

    @Test
    void authService_confirmRegistration_noUser() {
        when(authenticationManager.authenticate(Mockito.any())).thenReturn(TestMockUtils.getStubAuthInfo());

        RegistrationToken token = new RegistrationToken(null, 1L, null);
        when(registrationTokenRepository.findById(Mockito.any())).thenReturn(Optional.of(token));
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThat(authService.confirmRegistration("token")).isFalse();
    }

    @Test
    void authService_confirmRegistration_success() {

        RegistrationToken token = new RegistrationToken(null, 1L, null);
        when(registrationTokenRepository.findById(Mockito.any())).thenReturn(Optional.of(token));
        User testUser = new User();
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));

        assertThat(authService.confirmRegistration("token")).isTrue();
        assertThat(token.getStatus()).isEqualTo(User.Status.CONFIRMED);
        assertThat(testUser.getStatus()).isEqualTo(User.Status.CONFIRMED);
    }

    @Test
    void authService_login_success() {
        when(authenticationManager.authenticate(any())).thenReturn(TestMockUtils.getAuthInfo(TestMockUtils.getStubUser()));
        assertThat(authService.login("foo", "bar")).isNotNull();
    }

    @Test
    void authService_login_not_confirmed() {
        User user = TestMockUtils.getStubUser();
        user.setStatus(User.Status.NOT_CONFIRMED);
        when(authenticationManager.authenticate(any())).thenReturn(TestMockUtils.getAuthInfo(user));
        assertThat(authService.login("foo", "bar")).isNull();
    }

    @Test
    void authService_register_usernameAlreadyExists() {
        User user = new User();
        String username = "newUser";
        user.setUsername(username);
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(user));

        RegistrationResponse response = authService.register("test@mail.ru", username, "password");
        assertThat(response.getUuid()).isNull();
        assertThat(response.getData()).containsExactly(InvalidData.USERNAME);
    }

    @Test
    void authService_register_emailAlreadyExists() {
        User user = new User();
        user.setId(1L);
        String email = "newUser@mail.ru";
        user.setEmail(email);
        user.setStatus(User.Status.CONFIRMED);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));

        RegistrationResponse response = authService.register(email, "user", "password");
        assertThat(response.getUuid()).isNull();
        assertThat(response.getData()).containsExactly(InvalidData.EMAIL);
    }

    @Test
    void authService_register_success() {
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        User user = new User();
        user.setId(1L);
        when(userRepository.save(Mockito.any())).thenReturn(user);

        RegistrationResponse response = authService.register("testEmail@mail.ru", "user", "password");
        assertThat(response.getUuid()).isNotEmpty();
        assertThat(response.getData()).isEmpty();
        verify(mailSender, times(1)).send(ArgumentCaptor.forClass(SimpleMailMessage.class).capture());
    }

    @Test
    void authService_recovery_emptyMail() {
        assertThat(authService.recovery("")).isNull();
    }

    @Test
    void authService_recovery_nullMail() {
        assertThat(authService.recovery(null)).isNull();
    }

    @Test
    void authService_recovery_notFoundEmail() {
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        authService.recovery("test@mail.ru");
        verify(mailSender, never()).send(ArgumentCaptor.forClass(SimpleMailMessage.class).capture());
    }

    @Test
    void authService_recovery_success() {
        User user = new User();
        user.setStatus(User.Status.CONFIRMED);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        authService.recovery("test@mail.ru");
        verify(mailSender, times(1)).send(ArgumentCaptor.forClass(SimpleMailMessage.class).capture());
    }

    @Test
    void authService_confirmation_nullCode() {
        assertThat(authService.confirmation("asdasd", null)).isEmpty();
    }

    @Test
    void authService_confirmation_nullToken() {
        assertThat(authService.confirmation(null, 123321)).isEmpty();
    }

    @Test
    void authService_confirmation_recoveryTokenNotFound() {
        when(recoveryTokenRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        assertThat(authService.confirmation("asdasd", 123321)).isEmpty();
    }

    @Test
    void authService_confirmation_recoveryTokenIsUsed() {
        when(recoveryTokenRepository.findById(Mockito.any())).thenReturn(Optional.of(new RecoveryToken("sadsad", 1L, 1232, RecoveryToken.Stage.USED, 5)));
        assertThat(authService.confirmation("asdasd", 123321)).isEmpty();
    }

    @Test
    void authService_confirmation_recoveryTokenIsReady() {
        RecoveryToken recoveryToken = new RecoveryToken("sadsad", 1L, 1232, RecoveryToken.Stage.READY, 5);

        when(recoveryTokenRepository.findById(Mockito.any())).thenReturn(Optional.of(recoveryToken));
        assertThat(authService.confirmation("asdasd", 123321)).isNotEmpty();
        assertThat(authService.confirmation("asdasd", 123321).get().getUUID()).isEqualTo(recoveryToken.getUUID());
    }

    @Test
    void authService_confirmation_codeIsRight() {
        int code = 1232;
        RecoveryToken recoveryToken = new RecoveryToken("sadsad", 1L, code, RecoveryToken.Stage.NOT_CONFIRMED, 5);

        when(recoveryTokenRepository.findById(Mockito.any())).thenReturn(Optional.of(recoveryToken));

        Optional<RecoveryToken> result = authService.confirmation("asdasd", code);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getUUID()).isEqualTo(recoveryToken.getUUID());
        assertThat(recoveryToken.getStage()).isEqualTo(RecoveryToken.Stage.READY);
    }

    @Test
    void authService_confirmation_wrongCode() {
        int code = 1232;
        int triesLeft = 5;
        RecoveryToken recoveryToken = new RecoveryToken("sadsad", 1L, code, RecoveryToken.Stage.NOT_CONFIRMED, triesLeft);

        when(recoveryTokenRepository.findById(Mockito.any())).thenReturn(Optional.of(recoveryToken));
        Optional<RecoveryToken> result = authService.confirmation("asdasd", 123213213);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getUUID()).isEqualTo(recoveryToken.getUUID());
        assertThat(recoveryToken.getTriesLeft()).isEqualTo(triesLeft - 1);
    }

    @Test
    void authService_confirmation_triesExceeded() {
        int code = 1232;
        int triesLeft = 1;
        RecoveryToken recoveryToken = new RecoveryToken("sadsad", 1L, code, RecoveryToken.Stage.NOT_CONFIRMED, triesLeft);

        when(recoveryTokenRepository.findById(Mockito.any())).thenReturn(Optional.of(recoveryToken));
        Optional<RecoveryToken> result = authService.confirmation("asdasd", 123213213);

        assertThat(result).isEmpty();
        assertThat(recoveryToken.getStage()).isEqualTo(RecoveryToken.Stage.USED);
    }

    @Test
    void authService_setNewPassword_nullPassword() {
        assertThat(authService.setNewPassword("asdasd", null)).isEqualTo(AuthServiceImpl.RecoveryResponse.INVALID_TOKEN);
    }

    @Test
    void authService_setNewPassword_nullToken() {
        assertThat(authService.setNewPassword(null, "password")).isEqualTo(AuthServiceImpl.RecoveryResponse.INVALID_TOKEN);
    }

    @Test
    void authService_setNewPassword_emptyToken() {
        assertThat(authService.setNewPassword("", "password")).isEqualTo(AuthServiceImpl.RecoveryResponse.INVALID_TOKEN);
    }

    @Test
    void authService_setNewPassword_notConfirmedRecoveryToken() {
        RecoveryToken recoveryToken = new RecoveryToken("sadsad", 1L, 123, RecoveryToken.Stage.NOT_CONFIRMED, 5);

        when(recoveryTokenRepository.findById(Mockito.any())).thenReturn(Optional.of(recoveryToken));
        assertThat(authService.setNewPassword("token", "password")).isEqualTo(AuthServiceImpl.RecoveryResponse.FAILURE);
    }

    @Test
    void authService_setNewPassword_notFoundUserByToken() {
        RecoveryToken recoveryToken = new RecoveryToken("sadsad", 1L, 123, RecoveryToken.Stage.READY, 5);

        when(recoveryTokenRepository.findById(Mockito.any())).thenReturn(Optional.of(recoveryToken));
        when(userRepository.findById(recoveryToken.getUserId())).thenReturn(Optional.empty());
        assertThat(authService.setNewPassword("token", "password")).isEqualTo(AuthServiceImpl.RecoveryResponse.INVALID_TOKEN);
    }

    @Test
    void authService_setNewPassword_success() {
        String password = "password";
        RecoveryToken recoveryToken = new RecoveryToken("sadsad", 1L, 123, RecoveryToken.Stage.READY, 5);
        User user = new User();

        when(recoveryTokenRepository.findById(Mockito.any())).thenReturn(Optional.of(recoveryToken));
        when(userRepository.findById(recoveryToken.getUserId())).thenReturn(Optional.of(user));

        assertThat(authService.setNewPassword("token", password)).isEqualTo(AuthServiceImpl.RecoveryResponse.SUCCESS);
        assertThat(user.getPassword()).isNotEqualTo(password);
        assertThat(recoveryToken.getStage()).isEqualTo(RecoveryToken.Stage.USED);
    }

    @Test
    void CustomUserDetailsService_loadUserByUsername_OK() {
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(new User()));
        assertThat(userDetailsService.loadUserByUsername("foo")).isNotNull();
    }

    @Test
    void CustomUserDetailsService_getUserById_OK() {
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(new User()));
        assertThat(userDetailsService.getUserById(1L)).isNotNull();
    }

    @Test
    void JwtService_GenerateAuthInfo_OK() {
        User user = TestMockUtils.getStubUser();

        Assertions.assertThat(jwtService.generateAuthInfo(user)).isNotNull();
    }

}
