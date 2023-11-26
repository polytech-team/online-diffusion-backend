package team.polytech.online.diffusion.service.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.polytech.automatic.webui.api.DefaultApi;
import team.polytech.automatic.webui.invoker.ApiClient;
import team.polytech.automatic.webui.model.SDModelItem;
import team.polytech.automatic.webui.model.StableDiffusionProcessingTxt2Img;
import team.polytech.online.diffusion.entity.GenerationStatus;
import team.polytech.online.diffusion.entity.SDTxt2ImgRequest;
import team.polytech.online.diffusion.repository.GenerationStatusRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    private final DefaultApi automaticUiApi;
    private final StableDiffusionRequestSender sender;
    private final GenerationStatusRepository generationRepository;


    @Autowired
    public GeneratorServiceImpl(ApiClient client, StableDiffusionRequestSender sender,
                                GenerationStatusRepository generationRepository) {
        this.automaticUiApi = new DefaultApi(client);
        this.sender = sender;
        this.generationRepository = generationRepository;
    }

    @Override
    public GenerationStatus generate(String prompt, String antiPrompt, String modelName, long seed) {
        StableDiffusionProcessingTxt2Img request = new StableDiffusionProcessingTxt2Img();
        request.prompt(prompt);
        request.negativePrompt(antiPrompt);
        request.seed((int) seed);
        request.height(128);
        request.width(128);
        request.steps(5);
        request.batchSize(1);

        String uuid = UUID.randomUUID().toString();

        GenerationStatus result = generationRepository.save(new GenerationStatus(uuid, GenerationStatus.Stage.NOT_STARTED));
        sender.sendRequest(new SDTxt2ImgRequest(uuid, request));
        return result;
    }

    @Override
    public Optional<GenerationStatus> getGenerationStatus(String UUID) {
        return generationRepository.findById(UUID);
    }

    @Override
    public List<String> getModels() {
        return automaticUiApi.getSdModelsSdapiV1SdModelsGet().stream().map(SDModelItem::getModelName).toList();
    }
}
