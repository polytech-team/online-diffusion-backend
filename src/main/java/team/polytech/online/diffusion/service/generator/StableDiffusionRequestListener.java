package team.polytech.online.diffusion.service.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import team.polytech.automatic.webui.api.DefaultApi;
import team.polytech.automatic.webui.invoker.ApiClient;
import team.polytech.automatic.webui.model.Options;
import team.polytech.automatic.webui.model.SDModelItem;
import team.polytech.automatic.webui.model.TextToImageResponse;
import team.polytech.online.diffusion.config.SDQueueCfg;
import team.polytech.online.diffusion.entity.GenerationStatus;
import team.polytech.online.diffusion.entity.SDTxt2ImgRequest;
import team.polytech.online.diffusion.repository.GenerationStatusRepository;

@EnableRabbit
@Component
public class StableDiffusionRequestListener {
    private static final Logger LOG = LoggerFactory.getLogger(StableDiffusionRequestListener.class);

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
            Options options = automaticUiApi.getConfigSdapiV1OptionsGet();

            if (!isValidModel(request.getModel())) {
                LOG.warn("Invalid model in request: " + request.getModel());
                generationRepository.save(new GenerationStatus(request.getUUID(), GenerationStatus.Stage.FAILED));
                return;
            }

            String currentModel = (String) options.getSdModelCheckpoint();
            if (currentModel == null || !currentModel.equals(request.getModel())) {
                options.setSdModelCheckpoint(request.getModel());
                automaticUiApi.setConfigSdapiV1OptionsPost(options);
            }
            response = automaticUiApi.text2imgapiSdapiV1Txt2imgPost(request.getRequest());
        } catch (RestClientException restClientException) {
            LOG.error("Execution failed for stable-diffusion api request ", restClientException);
            generationRepository.save(new GenerationStatus(request.getUUID(), GenerationStatus.Stage.FAILED));
            return;
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
        return automaticUiApi.getSdModelsSdapiV1SdModelsGet()
                .stream()
                .map(SDModelItem::getModelName)
                .filter(model -> !model.equals(target))
                .toList().isEmpty();
    }

}
