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
import team.polytech.online.diffusion.api.GeneratorApiController;
import team.polytech.online.diffusion.entity.GenerationStatus;
import team.polytech.online.diffusion.service.generator.GeneratorService;
import team.polytech.online.diffusion.service.image.ImageService;
import team.polytech.online.diffusion.utils.TestMockUtils;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GeneratorApiTests {

    @Mock
    private GeneratorService generatorService;
    @Mock
    private ImageService imageService;
    @Mock
    private NativeWebRequest request;

    @InjectMocks
    private GeneratorApiController generatorApiController;

    @BeforeEach
    public void mockAuthInfo() {
        TestMockUtils.mockAuthInfo();
    }

    @Test
    public void GeneratorApiController_getGenerator_OK() {
        Mockito.when(generatorService.getModels()).thenReturn(List.of("sdklfjjdklsf"));

        Assertions.assertThat(generatorApiController.getGenerator()
                .getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void GeneratorApiController_postGenerator_OK() {
        Mockito.when(generatorService.generate(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(new GenerationStatus("sddsf", GenerationStatus.Stage.IN_PROGRESS));

        Assertions.assertThat(generatorApiController.postGenerator("foo", "boo", "too", Optional.of(1234))
                .getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void GeneratorApiController_generationStatus_OK() {
        Mockito.when(generatorService.getGenerationStatus(Mockito.any()))
                .thenReturn(Optional.of(new GenerationStatus("sddsf", GenerationStatus.Stage.IN_PROGRESS)));

        Assertions.assertThat(generatorApiController.generationStatus("foo").getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void GeneratorApiController_getRequest_OK() {
        Assertions.assertThat(generatorApiController.getRequest()).isNotNull();
    }

}
