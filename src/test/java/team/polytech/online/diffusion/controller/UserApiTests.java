package team.polytech.online.diffusion.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.NativeWebRequest;
import team.polytech.online.diffusion.api.UserApiController;
import team.polytech.online.diffusion.model.GalleryPagingWrapper;
import team.polytech.online.diffusion.model.ProfileInfo;
import team.polytech.online.diffusion.service.user.UserService;
import team.polytech.online.diffusion.utils.TestMockUtils;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserApiTests {

    @Mock
    private UserService userService;
    @Mock
    private NativeWebRequest request;

    @InjectMocks
    private UserApiController userController;

    @BeforeEach
    public void mockAuthInfo() {
        TestMockUtils.mockAuthInfo();
    }

    @Test
    public void UserApiController_profilePassword_OK() {
        Mockito.when(userService.changeUserPassword(Mockito.any(), Mockito.any())).thenReturn(true);

        Assertions.assertThat(userController.profilePassword("123").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void UserApiController_profilePassword_Error() {
        Mockito.when(userService.changeUserPassword(Mockito.any(), Mockito.any())).thenReturn(false);

        Assertions.assertThat(userController.profilePassword("123").getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void UserApiController_profileUsername_OK() {
        Mockito.when(userService.changeUsername(Mockito.any(), Mockito.any())).thenReturn(true);

        Assertions.assertThat(userController.profileUsername("123").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void UserApiController_profileUsername_Error() {
        Mockito.when(userService.changeUsername(Mockito.any(), Mockito.any())).thenReturn(false);

        Assertions.assertThat(userController.profileUsername("123").getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void UserApiController_getProfile_OK() {
        Mockito.when(userService.getProfileInfo(Mockito.any())).thenReturn(Optional.of(new ProfileInfo()));

        Assertions.assertThat(userController.getProfile().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void UserApiController_profileGallery_OK() {
        Mockito.when(userService.getGallery(Mockito.any())).thenReturn(new GalleryPagingWrapper());

        Assertions.assertThat(userController.profileGallery(Optional.empty()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void UserApiController_profileGallery_EmptyWrapper() {
        Mockito.when(userService.getGallery(Mockito.any())).thenReturn(null);

        Assertions.assertThat(userController.profileGallery(Optional.empty()).getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void UserApiController_getRequest_OK() {
        Assertions.assertThat(userController.getRequest()).isNotNull();
    }
}
