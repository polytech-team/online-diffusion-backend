/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.0.1).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package team.polytech.online.diffusion.api;

import team.polytech.online.diffusion.model.AuthInfo;
import team.polytech.online.diffusion.model.InvalidData;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-11-27T00:11:07.629235+03:00[Europe/Moscow]")
@Validated
@Tag(name = "Auth", description = "Методы, связанные с авторизацией")
public interface AuthApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * PUT /api/v1/auth/confirmation : Подтверждение правильности кода
     * 
     *
     * @param code Код для сброса пароля с электронной почты (required)
     * @param recoveryToken Токен, назначаемый сессии сброса пароля (required)
     * @return код правильный и соответсвует введеному recovery_token, можно присылать новый пароль через метод Confirm_new_Password (status code 200)
     *         or присылается, когда такой recovery_token есть и он валидный, однако код был введен неверно (status code 400)
     *         or такого recovery_token нет на сервере в принципе (status code 404)
     */
    @Operation(
        operationId = "confirmation",
        summary = "Подтверждение правильности кода",
        description = "",
        tags = { "Auth" },
        responses = {
            @ApiResponse(responseCode = "200", description = "код правильный и соответсвует введеному recovery_token, можно присылать новый пароль через метод Confirm_new_Password"),
            @ApiResponse(responseCode = "400", description = "присылается, когда такой recovery_token есть и он валидный, однако код был введен неверно", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))
            }),
            @ApiResponse(responseCode = "404", description = "такого recovery_token нет на сервере в принципе")
        },
        security = {
            @SecurityRequirement(name = "JWTAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/api/v1/auth/confirmation",
        produces = { "application/json" }
    )
    default ResponseEntity<Void> confirmation(
        @NotNull @Parameter(name = "code", description = "Код для сброса пароля с электронной почты", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "code", required = true) Integer code,
        @NotNull @Parameter(name = "recoveryToken", description = "Токен, назначаемый сессии сброса пароля", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "recoveryToken", required = true) String recoveryToken
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /api/v1/auth/login : Авторизация
     * 
     *
     * @param email Почта пользователя (required)
     * @param password Пароль пользователя (required)
     * @return все хорошо, авторизация прошла успешно (status code 200)
     *         or логин или пароль неверные (status code 400)
     */
    @Operation(
        operationId = "login",
        summary = "Авторизация",
        description = "",
        tags = { "Auth" },
        responses = {
            @ApiResponse(responseCode = "200", description = "все хорошо, авторизация прошла успешно", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = AuthInfo.class))
            }),
            @ApiResponse(responseCode = "400", description = "логин или пароль неверные")
        },
        security = {
            @SecurityRequirement(name = "JWTAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/api/v1/auth/login",
        produces = { "application/json" }
    )
    default ResponseEntity<AuthInfo> login(
        @NotNull @jakarta.validation.constraints.Email@Parameter(name = "email", description = "Почта пользователя", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "email", required = true) String email,
        @NotNull @Size(min = 8, max = 32) @Parameter(name = "password", description = "Пароль пользователя", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "password", required = true) String password
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"accessToken\" : \"accessToken\", \"refreshToken\" : \"refreshToken\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /api/v1/auth/new-password : Ввод нового пароля
     * 
     *
     * @param code Код для сброса пароля с электронной почты (required)
     * @param recoveryToken Токен, назначаемый сессии сброса пароля (required)
     * @return токен активирован и новый пароль успешно принят (status code 200)
     *         or такой токен есть, однако он еще не был активирован присыланием правильного кода в методе Confirm_Password_Code (status code 400)
     *         or такого recovery_token нет на сервере в принципе (status code 404)
     */
    @Operation(
        operationId = "newPassword",
        summary = "Ввод нового пароля",
        description = "",
        tags = { "Auth" },
        responses = {
            @ApiResponse(responseCode = "200", description = "токен активирован и новый пароль успешно принят"),
            @ApiResponse(responseCode = "400", description = "такой токен есть, однако он еще не был активирован присыланием правильного кода в методе Confirm_Password_Code"),
            @ApiResponse(responseCode = "404", description = "такого recovery_token нет на сервере в принципе")
        },
        security = {
            @SecurityRequirement(name = "JWTAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/api/v1/auth/new-password"
    )
    default ResponseEntity<Void> newPassword(
        @NotNull @Parameter(name = "code", description = "Код для сброса пароля с электронной почты", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "code", required = true) Integer code,
        @NotNull @Parameter(name = "recoveryToken", description = "Токен, назначаемый сессии сброса пароля", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "recoveryToken", required = true) String recoveryToken
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /api/v1/auth/recovery : Получение кода для восстановления на почту
     * 
     *
     * @param email Почта пользователя (required)
     * @return почта принята, код отправлен, если почта есть в базе (status code 200)
     *         or неккоректные данные, которые точно не являются почтовым адресом (status code 400)
     */
    @Operation(
        operationId = "recovery",
        summary = "Получение кода для восстановления на почту",
        description = "",
        tags = { "Auth" },
        responses = {
            @ApiResponse(responseCode = "200", description = "почта принята, код отправлен, если почта есть в базе", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "400", description = "неккоректные данные, которые точно не являются почтовым адресом")
        },
        security = {
            @SecurityRequirement(name = "JWTAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/api/v1/auth/recovery",
        produces = { "application/json" }
    )
    default ResponseEntity<String> recovery(
        @NotNull @jakarta.validation.constraints.Email@Parameter(name = "email", description = "Почта пользователя", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "email", required = true) String email
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /api/v1/auth/refresh-token : Обновление токена авторизации
     * 
     *
     * @param refreshToken Токен, используемый для обновления авторизации, когда accessToken кончился (required)
     * @return все хорошо, регистрация прошла успешно, юзер должен подтвердить почту (status code 200)
     *         or рефреш токен истек или невалиден (status code 404)
     */
    @Operation(
        operationId = "refreshToken",
        summary = "Обновление токена авторизации",
        description = "",
        tags = { "Auth" },
        responses = {
            @ApiResponse(responseCode = "200", description = "все хорошо, регистрация прошла успешно, юзер должен подтвердить почту", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = AuthInfo.class))
            }),
            @ApiResponse(responseCode = "404", description = "рефреш токен истек или невалиден")
        },
        security = {
            @SecurityRequirement(name = "JWTAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/api/v1/auth/refresh-token",
        produces = { "application/json" }
    )
    default ResponseEntity<AuthInfo> refreshToken(
        @NotNull @Parameter(name = "refreshToken", description = "Токен, используемый для обновления авторизации, когда accessToken кончился", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "refreshToken", required = true) String refreshToken
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"accessToken\" : \"accessToken\", \"refreshToken\" : \"refreshToken\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /api/v1/auth/register : Регистрация на портале
     * 
     *
     * @param email Почта пользователя (required)
     * @param username Имя пользователя (required)
     * @param password Пароль пользователя (required)
     * @return все хорошо, регистрация прошла успешно, юзер должен подтвердить почту (status code 200)
     *         or регистрация невозможна, так как какие-то из полей были некоректными (status code 400)
     *         or регистрация невозможна, так как некоторые поля удовлетворяют требованиям уникальности (status code 409)
     */
    @Operation(
        operationId = "register",
        summary = "Регистрация на портале",
        description = "",
        tags = { "Auth" },
        responses = {
            @ApiResponse(responseCode = "200", description = "все хорошо, регистрация прошла успешно, юзер должен подтвердить почту"),
            @ApiResponse(responseCode = "400", description = "регистрация невозможна, так как какие-то из полей были некоректными", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = InvalidData.class))
            }),
            @ApiResponse(responseCode = "409", description = "регистрация невозможна, так как некоторые поля удовлетворяют требованиям уникальности")
        },
        security = {
            @SecurityRequirement(name = "JWTAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/api/v1/auth/register",
        produces = { "application/json" }
    )
    default ResponseEntity<Void> register(
        @NotNull @jakarta.validation.constraints.Email@Parameter(name = "email", description = "Почта пользователя", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "email", required = true) String email,
        @NotNull @Size(min = 4, max = 32) @Parameter(name = "username", description = "Имя пользователя", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "username", required = true) String username,
        @NotNull @Size(min = 8, max = 32) @Parameter(name = "password", description = "Пароль пользователя", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "password", required = true) String password
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
