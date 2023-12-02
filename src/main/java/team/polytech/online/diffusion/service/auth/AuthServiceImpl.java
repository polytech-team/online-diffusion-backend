package team.polytech.online.diffusion.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.polytech.online.diffusion.entity.RecoveryToken;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.AuthInfo;
import team.polytech.online.diffusion.repository.RecoveryTokenRepository;
import team.polytech.online.diffusion.repository.UserRepository;

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

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           UserRepository userRepository, RecoveryTokenRepository recoveryRepository,
                           PasswordEncoder passwordEncoder, JavaMailSender sender) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = sender;
        this.recoveryRepository = recoveryRepository;
    }

    @Override
    public AuthInfo login(String email, String password) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = (User) auth.getPrincipal();
        return jwtService.generateAuthInfo(user);
    }

    @Override
    public User register(String email, String username, String password) {
        if (userRepository.findByUsername(username).isEmpty() && userRepository.findByEmail(email).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public String recovery(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        String uuid = UUID.randomUUID().toString();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            long recoveryCode = random.nextLong(100000, 1000000);

            sendRecoveryMessage(user.get(), recoveryCode);

            recoveryRepository.save(new RecoveryToken(uuid, recoveryCode, RecoveryToken.Stage.NOT_CONFIRMED));
        }

        // Умалчиваем, что не нашли email, чтобы вор думал, что все норм
        return uuid;
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

    private void sendRecoveryMessage(User user, long code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@imaginarium.com");
        message.setTo(user.getEmail());
        message.setSubject("Imaginarium recovery token");
        message.setText(String.format(
                """
                Hey %s!
                Looks like you forgot your password :(
                Here is the recovery code: %d
                If it wasn't you requesting password reset, you may just ignore this message
                This message is send automatically, so there is no use trying to reply
                """, user.getUsername(), code));
        mailSender.send(message);
    }
}
