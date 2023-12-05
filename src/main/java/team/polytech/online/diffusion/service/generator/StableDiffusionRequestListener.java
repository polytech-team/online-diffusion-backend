package team.polytech.online.diffusion.service.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import team.polytech.automatic.webui.api.DefaultApi;
import team.polytech.automatic.webui.invoker.ApiClient;
import team.polytech.automatic.webui.model.SDModelItem;
import team.polytech.automatic.webui.model.TextToImageResponse;
import team.polytech.online.diffusion.config.SDQueueCfg;
import team.polytech.online.diffusion.entity.GenerationStatus;
import team.polytech.online.diffusion.entity.SDTxt2ImgRequest;
import team.polytech.online.diffusion.repository.GenerationStatusRepository;

import java.util.Collections;
import java.util.Map;

@EnableRabbit
@Component
public class StableDiffusionRequestListener {
    private static final Logger LOG = LoggerFactory.getLogger(StableDiffusionRequestListener.class);

    @Value("${stable-diffusion.mock.enabled}")
    private boolean mockEnabled;
    @Value("${stable-diffusion.mock.time-ms}")
    private long mockTimeMs;
    @Value("${stable-diffusion.mock.image}")
    private String base64MockImage;
    @Value("${stable-diffusion.conf.easy-negative-enabled}")
    private boolean useEasyNegative;

    private final DefaultApi automaticUiApi;
    private final GenerationStatusRepository generationRepository;
    private final ImgurImageUploadService imageService;

    @Autowired
    public StableDiffusionRequestListener(ApiClient client,
                                          GenerationStatusRepository generationRepository,
                                          ImgurImageUploadService imageService) {
        automaticUiApi = new DefaultApi(client);
        this.generationRepository = generationRepository;
        this.imageService = imageService;
    }

    @RabbitListener(queues = SDQueueCfg.ROUTING_KEY)
    public void processGenerationRequest(SDTxt2ImgRequest request) {
        generationRepository.save(new GenerationStatus(request.getUUID(), GenerationStatus.Stage.IN_PROGRESS));
        TextToImageResponse response;

        try {
            if (!isValidModel(request.getModel())) {
                LOG.warn("Invalid model in request: " + request.getModel());
                generationRepository.save(new GenerationStatus(request.getUUID(), GenerationStatus.Stage.FAILED));
                return;
            }

            Map<String, Object> options = automaticUiApi.getConfigSdapiV1OptionsGet();

            String currentModel = (String) options.get("sd_model_checkpoint");
            if (currentModel == null || !currentModel.contains(request.getModel())) {
                options.put("sd_model_checkpoint", request.getModel());
                automaticUiApi.setConfigSdapiV1OptionsPost(options);
            }

            if (useEasyNegative) {
                String prompt = "(EasyNegative)," + request.getRequest().getNegativePrompt();
                request.getRequest().setNegativePrompt(prompt);
            }

            response = automaticUiApi.text2imgapiSdapiV1Txt2imgPost(request.getRequest());
        } catch (RestClientException restClientException) {
            if (!mockEnabled) {
                LOG.error("Execution failed for stable-diffusion api request ", restClientException);
                generationRepository.save(new GenerationStatus(request.getUUID(), GenerationStatus.Stage.FAILED));
                return;
            }
            LOG.info("Stable diffusion is out! Using mock for dummy image, no actual image present. Sleeping for " + mockTimeMs);
            try {
                Thread.sleep(mockTimeMs);
            } catch (InterruptedException e) {
                LOG.warn("Sleeping interrupted");
                throw new RuntimeException(e);
            }
            response = new TextToImageResponse();
            response.setImages(Collections.singletonList(base64MockImage));
        }

        if (response.getImages() == null || response.getImages().isEmpty() || response.getImages().size() > 1) {
            LOG.warn("Request was successful, yet no images or too much images were generated. Response info: " + response.getInfo());
            generationRepository.save(new GenerationStatus(request.getUUID(), GenerationStatus.Stage.FAILED));
            return;
        }

        LOG.info("Image generation was a success! Passing to an Imgur service...");
        imageService.imageUploadTask(request, response.getImages().get(0));
    }

    private boolean isValidModel(String target) {
        return !automaticUiApi.getSdModelsSdapiV1SdModelsGet()
                .stream()
                .map(SDModelItem::getModelName)
                .filter(model -> !model.equals(target))
                .toList().isEmpty();
    }

}
