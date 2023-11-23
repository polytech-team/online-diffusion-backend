package team.polytech.online.diffusion.api;

import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.AuthInfo;
import team.polytech.online.diffusion.model.InvalidData;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.NativeWebRequest;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.annotation.Generated;
import team.polytech.online.diffusion.service.AuthService;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-10-26T02:19:33.552470+03:00[Europe/Moscow]")
@Controller
@RequestMapping("${openapi.onlineDiffusion.base-path:}")
public class AuthApiController implements AuthApi {

    private final NativeWebRequest request;
    private final AuthService authService;

    @Autowired
    public AuthApiController(NativeWebRequest request,
                             AuthService authService) {
        this.request = request;
        this.authService = authService;
    }

    @Override
    public ResponseEntity<AuthInfo> login(String email, String password) {
        //TODO Также адекватное отправление разных кодов ответа нужно
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
        User user = authService.register(email, username, password);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
