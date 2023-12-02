package team.polytech.online.diffusion.api;

import jakarta.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import team.polytech.online.diffusion.model.AuthInfo;
import team.polytech.online.diffusion.service.auth.AuthService;
import team.polytech.online.diffusion.service.auth.AuthServiceImpl;

import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-10-26T02:19:33.552470+03:00[Europe/Moscow]")
@Controller
@RequestMapping("${openapi.onlineDiffusion.base-path:}")
public class AuthApiController implements AuthApi {

    private final NativeWebRequest request;
    private final AuthService authService;
    private final TemplateEngine htmlTemplateEngine;

    @Autowired
    public AuthApiController(NativeWebRequest request,
                             AuthService authService,
                             TemplateEngine htmlTemplateEngine) {
        this.request = request;
        this.authService = authService;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    @Override
    public ResponseEntity<String> emailConfirmation(String uuid) {
        return authService.confirmRegistration(uuid) ?
                new ResponseEntity<>(htmlTemplateEngine.process("auth/success.html", new Context()), HttpStatus.OK)
                : new ResponseEntity<>(htmlTemplateEngine.process("auth/failure.html", new Context()), HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<AuthInfo> login(String email, String password) {
        AuthInfo info = authService.login(email, password);
        if (info == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AuthInfo> refreshToken(String refreshToken) {
        AuthInfo info = authService.refresh(refreshToken);
        if (info == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> register(String email, String username, String password) {
        //TODO Также адекватное отправление разных кодов ответа нужно
        String uuid = authService.register(email, username, password);
        if (uuid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> recovery(String email) {
        String recoveryId = authService.recovery(email);
        return recoveryId == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(recoveryId, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> confirmation(Integer code, String recoveryToken) {
        AuthServiceImpl.RecoveryResponse response = authService.confirmation(recoveryToken, code);
        return switch (response) {
            case SUCCESS -> new ResponseEntity<>(HttpStatus.OK);
            case FAILURE -> new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            case INVALID_TOKEN -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
        };
    }

    @Override
    public ResponseEntity<Void> newPassword(String password, String recoveryToken) {
        AuthServiceImpl.RecoveryResponse response = authService.setNewPassword(recoveryToken, password);
        return switch (response) {
            case SUCCESS -> new ResponseEntity<>(HttpStatus.OK);
            case FAILURE -> new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            case INVALID_TOKEN -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
        };
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
