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
import team.polytech.online.diffusion.entity.RecoveryToken;
import team.polytech.online.diffusion.model.AuthInfo;
import team.polytech.online.diffusion.model.InvalidData;
import team.polytech.online.diffusion.service.auth.AuthService;
import team.polytech.online.diffusion.service.auth.AuthServiceImpl;
import team.polytech.online.diffusion.service.auth.response.RegistrationResponse;

import java.util.List;
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
    public ResponseEntity<List<String>> register(String email, String username, String password) {
        RegistrationResponse response = authService.register(email, username, password);
        if (response.getUuid() == null) {
            return new ResponseEntity<>(toString(response.getData()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> recovery(String email) {
        String recoveryId = authService.recovery(email);
        return recoveryId == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(recoveryId, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> confirmation(Integer code, String recoveryToken) {
        Optional<RecoveryToken> response = authService.confirmation(recoveryToken, code);
        return response.map(token -> switch (token.getStage()) {
            case NOT_CONFIRMED -> new ResponseEntity<>(response.get().getTriesLeft(), HttpStatus.BAD_REQUEST);
            case READY -> new ResponseEntity<Integer>(HttpStatus.OK);
            case USED -> new ResponseEntity<Integer>(HttpStatus.NOT_FOUND);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

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

    private List<String> toString(List<InvalidData> data) {
        return data.stream().map(InvalidData::getValue).toList();
    }

}
