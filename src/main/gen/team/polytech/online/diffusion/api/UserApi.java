/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.0.1).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package team.polytech.online.diffusion.api;

import team.polytech.online.diffusion.model.GalleryPagingWrapper;
import team.polytech.online.diffusion.model.ProfileInfo;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
@Validated
@Tag(name = "User", description = "Методы, связанные с получением пользовательских данных")
public interface UserApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /api/v1/profile : Получаем инфо из профиля
     * 
     *
     * @return все хорошо, информация по юзеру получена (status code 200)
     *         or Попытка обратиться к защищенному JWT токеном эндпоинту без авторизации (status code 401)
     */
    @Operation(
        operationId = "getProfile",
        summary = "Получаем инфо из профиля",
        description = "",
        tags = { "User" },
        responses = {
            @ApiResponse(responseCode = "200", description = "все хорошо, информация по юзеру получена", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileInfo.class))
            }),
            @ApiResponse(responseCode = "401", description = "Попытка обратиться к защищенному JWT токеном эндпоинту без авторизации")
        },
        security = {
            @SecurityRequirement(name = "JWTAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/api/v1/profile",
        produces = { "application/json" }
    )
    default ResponseEntity<ProfileInfo> getProfile(
        
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"avatarUrl\" : \"avatarUrl\", \"generated\" : 0, \"email\" : \"email\", \"username\" : \"username\", \"gallery_images\" : 6, \"posted\" : 1 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /api/v1/profile/gallery : Получить галерею
     * 
     *
     * @param marker Маркер, показывающий начиная с какого id подгружать ресурсы (optional)
     * @return все хорошо, присланы изображения галереи (status code 200)
     *         or такой маркер не найден (status code 404)
     *         or Попытка обратиться к защищенному JWT токеном эндпоинту без авторизации (status code 401)
     */
    @Operation(
        operationId = "profileGallery",
        summary = "Получить галерею",
        description = "",
        tags = { "User" },
        responses = {
            @ApiResponse(responseCode = "200", description = "все хорошо, присланы изображения галереи", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = GalleryPagingWrapper.class))
            }),
            @ApiResponse(responseCode = "404", description = "такой маркер не найден"),
            @ApiResponse(responseCode = "401", description = "Попытка обратиться к защищенному JWT токеном эндпоинту без авторизации")
        },
        security = {
            @SecurityRequirement(name = "JWTAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/api/v1/profile/gallery",
        produces = { "application/json" }
    )
    default ResponseEntity<GalleryPagingWrapper> profileGallery(
        @Parameter(name = "marker", description = "Маркер, показывающий начиная с какого id подгружать ресурсы", in = ParameterIn.QUERY) @Valid @RequestParam(value = "marker", required = false) Optional<Long> marker
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"nextMarker\" : 0.8008281904610115, \"images\" : [ { \"seed\" : \"seed\", \"authorName\" : \"authorName\", \"authorAvatarUrl\" : \"authorAvatarUrl\", \"photoId\" : 0.8008281904610115, \"anti-prompt\" : \"anti-prompt\", \"model\" : \"model\", \"prompt\" : \"prompt\" }, { \"seed\" : \"seed\", \"authorName\" : \"authorName\", \"authorAvatarUrl\" : \"authorAvatarUrl\", \"photoId\" : 0.8008281904610115, \"anti-prompt\" : \"anti-prompt\", \"model\" : \"model\", \"prompt\" : \"prompt\" } ] }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /api/v1/profile/password : Меняем пароль юзеру
     * 
     *
     * @param password Пароль пользователя (required)
     * @return все хорошо, пароль был успешно изменен (status code 200)
     *         or Попытка обратиться к защищенному JWT токеном эндпоинту без авторизации (status code 401)
     */
    @Operation(
        operationId = "profilePassword",
        summary = "Меняем пароль юзеру",
        description = "",
        tags = { "User" },
        responses = {
            @ApiResponse(responseCode = "200", description = "все хорошо, пароль был успешно изменен"),
            @ApiResponse(responseCode = "401", description = "Попытка обратиться к защищенному JWT токеном эндпоинту без авторизации")
        },
        security = {
            @SecurityRequirement(name = "JWTAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/api/v1/profile/password"
    )
    default ResponseEntity<Void> profilePassword(
        @NotNull @Size(min = 8, max = 32) @Parameter(name = "password", description = "Пароль пользователя", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "password", required = true) String password
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /api/v1/profile/username : Меняем имя юзеру
     * 
     *
     * @param username Имя пользователя (required)
     * @return все хорошо, имя пользователя было успешно изменено (status code 200)
     *         or изменение имени пользователя на данное имя невозможно, так как не удовлетворяет требованиям к уникальности (status code 400)
     *         or Попытка обратиться к защищенному JWT токеном эндпоинту без авторизации (status code 401)
     */
    @Operation(
        operationId = "profileUsername",
        summary = "Меняем имя юзеру",
        description = "",
        tags = { "User" },
        responses = {
            @ApiResponse(responseCode = "200", description = "все хорошо, имя пользователя было успешно изменено"),
            @ApiResponse(responseCode = "400", description = "изменение имени пользователя на данное имя невозможно, так как не удовлетворяет требованиям к уникальности"),
            @ApiResponse(responseCode = "401", description = "Попытка обратиться к защищенному JWT токеном эндпоинту без авторизации")
        },
        security = {
            @SecurityRequirement(name = "JWTAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/api/v1/profile/username"
    )
    default ResponseEntity<Void> profileUsername(
        @NotNull @Size(min = 4, max = 32) @Parameter(name = "username", description = "Имя пользователя", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "username", required = true) String username
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
