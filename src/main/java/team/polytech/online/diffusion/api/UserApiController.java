package team.polytech.online.diffusion.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.GalleryPagingWrapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.NativeWebRequest;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.annotation.Generated;
import team.polytech.online.diffusion.service.UserServer;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-10-26T02:19:33.552470+03:00[Europe/Moscow]")
@Controller
@RequestMapping("${openapi.onlineDiffusion.base-path:}")
public class UserApiController implements UserApi {

    private final NativeWebRequest request;
    @Autowired
    private final UserServer userServer;

    @Autowired
    public UserApiController(NativeWebRequest request, UserServer userServer) {
        this.request = request;
        this.userServer = userServer;

    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }


    @Override
    public ResponseEntity<Void> profilePassword(String newPassword) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean passwordChanged = userServer.changeUserPassword(username, newPassword);

        if (passwordChanged) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Void> profileUsername(String newUsername) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 执行用户名更改操作，使用 userId 作为标识
        boolean usernameChanged = userServer.changeUsername(username, newUsername);
        if (usernameChanged) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // 用户不存在或用户名更改失败
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
