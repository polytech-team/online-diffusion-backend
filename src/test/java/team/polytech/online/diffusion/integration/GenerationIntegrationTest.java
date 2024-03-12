package team.polytech.online.diffusion.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import team.polytech.automatic.webui.api.DefaultApi;
import team.polytech.online.diffusion.api.GeneratorApi;
import team.polytech.online.diffusion.entity.GenerationStatus;
import team.polytech.online.diffusion.repository.GenerationStatusRepository;
import team.polytech.online.diffusion.repository.ImageRepository;
import team.polytech.online.diffusion.repository.UserRepository;
import team.polytech.online.diffusion.utils.TestMockUtils;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Tag("IntegrationTest")
public class GenerationIntegrationTest {
    //Data-storage mocks
    @MockBean
    private GenerationStatusRepository generationStatusRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ImageRepository imageRepository;

    //External API mocks
    @MockBean
    private RabbitTemplate rabbitTemplate;
    @MockBean
    private DefaultApi automaticUIApi;


    @Autowired
    private GeneratorApi generatorApiController;

    @BeforeEach
    public void mockAuthInfo() {
        TestMockUtils.mockAuthInfo();
    }

    @Test
    public void GenerationIntegrationTest_getGenerator_OK() {
        Assertions.assertThat(generatorApiController.getGenerator()
                .getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void GenerationIntegrationTest_postGenerator_OK() {
        TestMockUtils.mockStubUserByUsername(userRepository);
        Mockito.when(generationStatusRepository.save(Mockito.any(GenerationStatus.class)))
                        .thenAnswer(var -> var.getArgument(0));

        Assertions.assertThat(generatorApiController.postGenerator("foo", "boo", "too", Optional.empty())
                .getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void GenerationIntegrationTest_postGenerator_INVAlID_USER() {
        Mockito.when(generationStatusRepository.save(Mockito.any(GenerationStatus.class)))
                .thenAnswer(var -> var.getArgument(0));

        Assertions.assertThat(generatorApiController.postGenerator("foo", "boo", "too", Optional.empty())
                .getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void GenerationIntegrationTest_generationStatus_NullStatus() {
        TestMockUtils.mockStubUserByUsername(userRepository);

        Mockito.when(generationStatusRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        Assertions.assertThat(generatorApiController.generationStatus("foo").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void GenerationIntegrationTest_generationStatus_NOT_STARTED() {
        TestMockUtils.mockStubUserByUsername(userRepository);

        Mockito.when(generationStatusRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new GenerationStatus("sddsf", GenerationStatus.Stage.NOT_STARTED)));

        Assertions.assertThat(generatorApiController.generationStatus("foo").getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void GenerationIntegrationTest_generationStatus_IN_PROGRESS() {
        TestMockUtils.mockStubUserByUsername(userRepository);

        Mockito.when(generationStatusRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new GenerationStatus("sddsf", GenerationStatus.Stage.IN_PROGRESS)));

        Assertions.assertThat(generatorApiController.generationStatus("foo").getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void GenerationIntegrationTest_generationStatus_GenerationSuccess_ImageFound() {
        TestMockUtils.mockStubUserByUsername(userRepository);

        Mockito.when(generationStatusRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new GenerationStatus("sddsf", GenerationStatus.Stage.SUCCESSFUL, Long.MIN_VALUE)));
        Mockito.when(imageRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(TestMockUtils.getStubImageEntity()));

        Assertions.assertThat(generatorApiController.generationStatus("foo").getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void GenerationIntegrationTest_generationStatus_GenerationSuccess_NoImageFound() {
        TestMockUtils.mockStubUserByUsername(userRepository);

        Mockito.when(generationStatusRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new GenerationStatus("sddsf", GenerationStatus.Stage.SUCCESSFUL, Long.MIN_VALUE)));
        Mockito.when(imageRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        Assertions.assertThat(generatorApiController.generationStatus("foo").getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void GenerationIntegrationTest_generationStatus_GenerationFailed() {
        TestMockUtils.mockStubUserByUsername(userRepository);

        Mockito.when(generationStatusRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new GenerationStatus("sddsf", GenerationStatus.Stage.FAILED)));

        Assertions.assertThat(generatorApiController.generationStatus("foo").getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
