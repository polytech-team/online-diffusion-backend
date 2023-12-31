package team.polytech.online.diffusion.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import team.polytech.online.diffusion.entity.RecoveryToken;
import team.polytech.online.diffusion.entity.RegistrationToken;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.AuthInfo;
import team.polytech.online.diffusion.model.InvalidData;
import team.polytech.online.diffusion.repository.RecoveryTokenRepository;
import team.polytech.online.diffusion.repository.RegistrationTokenRepository;
import team.polytech.online.diffusion.repository.UserRepository;
import team.polytech.online.diffusion.service.auth.response.RegistrationResponse;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthServiceImpl implements AuthService {

    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final RecoveryTokenRepository recoveryRepository;
    private final RegistrationTokenRepository registrationRepository;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           UserRepository userRepository, RecoveryTokenRepository recoveryRepository,
                           PasswordEncoder passwordEncoder, JavaMailSender sender,
                           RegistrationTokenRepository registrationRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = sender;
        this.recoveryRepository = recoveryRepository;
        this.registrationRepository = registrationRepository;
    }

    @Override
    public AuthInfo login(String email, String password) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = (User) auth.getPrincipal();
        if (user.getStatus() == User.Status.NOT_CONFIRMED) {
            return null;
        }
        return jwtService.generateAuthInfo(user);
    }

    @Override
    public boolean confirmRegistration(String token) {
        if (token == null) {
            return false;
        }

        Optional<RegistrationToken> tokenOptional = registrationRepository.findById(token);

        if (tokenOptional.isEmpty()) {
            return false;
        }

        RegistrationToken registrationToken = tokenOptional.get();

        Optional<User> userOpt = userRepository.findById(registrationToken.getUserId());

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        user.setStatus(User.Status.CONFIRMED);

        userRepository.save(user);

        registrationToken.setStatus(User.Status.CONFIRMED);
        registrationRepository.save(registrationToken);

        return true;
    }

    @Override
    public RegistrationResponse register(String email, String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return new RegistrationResponse(null, Collections.singletonList(InvalidData.USERNAME));
        }
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent() && userOpt.get().getStatus() != User.Status.NOT_CONFIRMED) {
            return new RegistrationResponse(null, Collections.singletonList(InvalidData.EMAIL));
        }

        User user = userOpt.orElseGet(User::new);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setStatus(User.Status.NOT_CONFIRMED);
        user = userRepository.save(user);

        String uuid = UUID.randomUUID().toString();
        registrationRepository.save(new RegistrationToken(uuid, user.getId(), User.Status.NOT_CONFIRMED));
        sendRegistrationMessage(user, ServletUriComponentsBuilder.fromCurrentContextPath().path("/auth/confirmation/" + uuid).build().toUriString());
        return new RegistrationResponse(uuid, Collections.emptyList());
    }

    @Override
    public String recovery(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        String uuid = UUID.randomUUID().toString();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && user.get().getStatus() == User.Status.CONFIRMED) {
            int recoveryCode = random.nextInt(100000, 1000000);

            sendRecoveryMessage(user.get(), recoveryCode);

            recoveryRepository.save(new RecoveryToken(uuid, user.get().getId(),
                    recoveryCode, RecoveryToken.Stage.NOT_CONFIRMED, 3));
        }

        // Умалчиваем, что не нашли email, чтобы вор думал, что все норм
        return uuid;
    }

    @Override
    public Optional<RecoveryToken> confirmation(String token, Integer code) {
        if (code == null) {
            return Optional.empty();
        }

        RecoveryToken recoveryToken = getToken(token);

        if (recoveryToken == null) {
            return Optional.empty();
        }

        if (recoveryToken.getStage() == RecoveryToken.Stage.READY) {
            return Optional.of(recoveryToken);
        }

        if (code.equals(recoveryToken.getCode())) {
            recoveryToken.setStage(RecoveryToken.Stage.READY);
            recoveryRepository.save(recoveryToken);
            return Optional.of(recoveryToken);
        }

        recoveryToken.setTriesLeft(recoveryToken.getTriesLeft() - 1);

        if (recoveryToken.getTriesLeft() > 0) {
            recoveryRepository.save(recoveryToken);
            return Optional.of(recoveryToken);
        }

        recoveryToken.setStage(RecoveryToken.Stage.USED);
        recoveryRepository.save(recoveryToken);

        return Optional.empty();
    }

    @Override
    public RecoveryResponse setNewPassword(String token, String password) {
        if (password == null) {
            return RecoveryResponse.INVALID_TOKEN;
        }

        RecoveryToken recoveryToken = getToken(token);

        if (recoveryToken == null) {
            return RecoveryResponse.INVALID_TOKEN;
        }

        if (recoveryToken.getStage() == RecoveryToken.Stage.NOT_CONFIRMED) {
            return RecoveryResponse.FAILURE;
        }

        Optional<User> userOpt = userRepository.findById(recoveryToken.getUserId());

        if (userOpt.isEmpty()) {
            return RecoveryResponse.INVALID_TOKEN;
        }

        User user = userOpt.get();

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        recoveryToken.setStage(RecoveryToken.Stage.USED);
        recoveryRepository.save(recoveryToken);

        return RecoveryResponse.SUCCESS;
    }

    private RecoveryToken getToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        Optional<RecoveryToken> recoveryTokenOpt = recoveryRepository.findById(token);

        if (recoveryTokenOpt.isEmpty()) {
            return null;
        }

        RecoveryToken recoveryToken = recoveryTokenOpt.get();

        if (recoveryToken.getStage() == RecoveryToken.Stage.USED) {
            return null;
        }

        return recoveryToken;
    }

    @Override
    public AuthInfo refresh(String refreshToken) {
        Long userId = jwtService.getUserIdFromRefresh(refreshToken);
        if (userId != null) {
            Optional<User> user = userRepository.findById(userId);
            return user.map(jwtService::generateAuthInfo).orElse(null);
        }
        return null;
    }

    private void sendRegistrationMessage(User user, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@imaginarium.com");
        message.setTo(user.getEmail());
        message.setSubject("Подтверждение Email в Imaginarium");
        message.setText(String.format(
                """
                Привет, %s!
                Очень рады, что вас заинтересовал наш сервис!
                Вот ссылка, чтобы подтвердить регистрацию: %s
                Это сообщение было создано и отправлено автоматически, поэтому не пытайся на него ответить
                """, user.getUsername(), link));
        mailSender.send(message);
    }

    private void sendRecoveryMessage(User user, long code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@imaginarium.com");
        message.setTo(user.getEmail());
        message.setSubject("Imaginarium recovery token");
        message.setText(String.format(
                """
                Привет, %s!
                Судя по всему ты забыл свой пароль :(
                Ничего страшного, вот код для сброса: %d
                Если это не ты запрашивал сброс пароля, сообщение можно просто проигнорировать
                Это сообщение было создано и отправлено автоматически, поэтому не пытайся на него ответить
                """, user.getUsername(), code));
        mailSender.send(message);
    }

    public enum RecoveryResponse {
        SUCCESS, FAILURE, INVALID_TOKEN
    }
}
