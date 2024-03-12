package team.polytech.online.diffusion.unit.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import team.polytech.imgur.api.ImageApi;
import team.polytech.imgur.model.Image;
import team.polytech.imgur.model.ImageResponse;
import team.polytech.online.diffusion.entity.GenerationStatus;
import team.polytech.online.diffusion.entity.ImageEntity;
import team.polytech.online.diffusion.entity.SDTxt2ImgRequest;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.repository.GenerationStatusRepository;
import team.polytech.online.diffusion.repository.ImageRepository;
import team.polytech.online.diffusion.repository.UserRepository;
import team.polytech.online.diffusion.service.generator.GeneratorService;
import team.polytech.online.diffusion.service.generator.StableDiffusionRequestListener;
import team.polytech.online.diffusion.utils.TestMockUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Tag("UnitTest")
public class GeneratorServiceTests {
    @MockBean
    private GenerationStatusRepository generationStatusRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ImageRepository imageRepository;
    @MockBean
    private RabbitTemplate rabbitTemplate;
    @MockBean
    private ImageApi imgurApi;
    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private StableDiffusionRequestListener listener;


    @Test
    public void GeneratorService_generate_generatesImage() throws InterruptedException {
        User user = TestMockUtils.mockStubUserByUsername(userRepository);
        TestMockUtils.mockStubUserById(userRepository);
        AtomicBoolean failed = new AtomicBoolean(true);

        TestMockUtils.mockAuthInfo(user);

        AtomicReference<GenerationStatus.Stage> previousStage = new AtomicReference<>(null);
        Mockito.when(generationStatusRepository.save(Mockito.any(GenerationStatus.class)))
                        .thenAnswer(var -> {
                            GenerationStatus status = var.getArgument(0);

                            switch (status.getStage()) {
                                case NOT_STARTED ->
                                        Assertions.assertThat(previousStage.get()).isNull();
                                case IN_PROGRESS ->
                                        Assertions.assertThat(previousStage.get()).isEqualTo(GenerationStatus.Stage.NOT_STARTED);
                                case SUCCESSFUL -> {
                                    Assertions.assertThat(previousStage.get()).isEqualTo(GenerationStatus.Stage.IN_PROGRESS);
                                    failed.set(false);
                                }
                                case FAILED ->
                                        Assertions.fail("Failed to generate image");
                            }

                            previousStage.set(status.getStage());

                            return status;
                        });

        Mockito.doAnswer(var -> {
            SDTxt2ImgRequest request = var.getArgument(1);
            listener.processGenerationRequest(request);
            return null;
        }).when(rabbitTemplate).convertAndSend(Mockito.anyString(), Mockito.any(SDTxt2ImgRequest.class));

        Mockito.when(imgurApi.uploadImage(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenAnswer(api -> {
                    ImageResponse response = new ImageResponse();
                    response.setSuccess(true);

                    Image image = new Image();
                    image.setLink("testlink");
                    response.setData(image);

                    return response;
                });

        Mockito.when(imageRepository.save(Mockito.any())).thenAnswer(var -> {
            ImageEntity entity = new ImageEntity();
            entity.setId(23L);
            return entity;
        });

        Assertions.assertThat(generatorService.generate("lala", "nono", "anime-diffusion", 2356)).isNotNull();
        Thread.sleep(500);
        Assertions.assertThat(failed.get()).isFalse();
    }

    @Test
    public void GeneratorService_generate_userNotFound() {
        TestMockUtils.mockAuthInfo(TestMockUtils.getStubUser());
        Assertions.assertThat(generatorService.generate("lala", "nono", "anime-diffusion", 2356)).isNull();
    }

    @Test
    public void GeneratorService_getGenerationStatus_returnsStatus() {
        Mockito.when(generationStatusRepository.findById(Mockito.any()))
                .thenAnswer(var -> Optional.of(new GenerationStatus("lkdjsf", GenerationStatus.Stage.NOT_STARTED)));

        Optional<GenerationStatus> status = generatorService.getGenerationStatus("foo");

        Assertions.assertThat(status.isPresent()).isTrue();
        Assertions.assertThat(status.get().getStage()).isEqualTo(GenerationStatus.Stage.NOT_STARTED);
    }

    @Test
    public void GeneratorService_getModels_returnsModels() {
        List<String> models = generatorService.getModels();

        Assertions.assertThat(models).isNotNull();
        Assertions.assertThat(models.size()).isEqualTo(2);
        Assertions.assertThat(models.get(0)).isEqualTo("anime-diffusion");
        Assertions.assertThat(models.get(1)).isEqualTo("mega-models");
    }

}
