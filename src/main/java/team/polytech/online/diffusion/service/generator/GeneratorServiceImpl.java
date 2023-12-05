package team.polytech.online.diffusion.service.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import team.polytech.automatic.webui.api.DefaultApi;
import team.polytech.automatic.webui.invoker.ApiClient;
import team.polytech.automatic.webui.model.SDModelItem;
import team.polytech.automatic.webui.model.StableDiffusionProcessingTxt2Img;
import team.polytech.online.diffusion.entity.GenerationStatus;
import team.polytech.online.diffusion.entity.SDTxt2ImgRequest;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.repository.GenerationStatusRepository;
import team.polytech.online.diffusion.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Value("${stable-diffusion.mock.enabled}")
    private boolean mockEnabled;
    private final DefaultApi automaticUiApi;
    private final StableDiffusionRequestSender sender;
    private final GenerationStatusRepository generationRepository;
    private final UserRepository userRepository;


    @Autowired
    public GeneratorServiceImpl(ApiClient client, StableDiffusionRequestSender sender,
                                GenerationStatusRepository generationRepository,
                                UserRepository userRepository) {
        this.automaticUiApi = new DefaultApi(client);
        this.sender = sender;
        this.generationRepository = generationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public GenerationStatus generate(String prompt, String antiPrompt, String modelName, int seed) {
        Optional<User> userOptional = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (userOptional.isEmpty()) {
            return null;
        }

        StableDiffusionProcessingTxt2Img request = new StableDiffusionProcessingTxt2Img();
        request.prompt(prompt);
        request.negativePrompt(antiPrompt);
        request.seed(seed);
        request.height(512);
        request.width(512);
        request.steps(25);
        request.batchSize(1);

        String uuid = UUID.randomUUID().toString();

        GenerationStatus result = generationRepository.save(new GenerationStatus(uuid, GenerationStatus.Stage.NOT_STARTED));
        sender.sendRequest(new SDTxt2ImgRequest(uuid, userOptional.get().getId(), modelName, request));
        return result;
    }

    @Override
    public Optional<GenerationStatus> getGenerationStatus(String UUID) {
        return generationRepository.findById(UUID);
    }

    @Override
    public List<String> getModels() {
        try {
            return automaticUiApi.getSdModelsSdapiV1SdModelsGet().stream().map(SDModelItem::getModelName).toList();
        } catch (Exception e) {
            if (mockEnabled) {
                return List.of("anime-diffusion", "mega-models");
            }
            throw e;
        }
    }
}
