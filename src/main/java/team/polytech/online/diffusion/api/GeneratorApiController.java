package team.polytech.online.diffusion.api;


import jakarta.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import team.polytech.online.diffusion.entity.GenerationStatus;
import team.polytech.online.diffusion.model.Image;
import team.polytech.online.diffusion.service.generator.GeneratorService;
import team.polytech.online.diffusion.service.image.ImageService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-10-26T02:19:33.552470+03:00[Europe/Moscow]")
@Controller
@RequestMapping("${openapi.onlineDiffusion.base-path:}")
public class GeneratorApiController implements GeneratorApi {

    private final NativeWebRequest request;
    private final GeneratorService generatorService;
    private final ImageService imageService;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    @Autowired
    public GeneratorApiController(NativeWebRequest request,
                                  GeneratorService generatorService,
                                  ImageService imageService) {
        this.request = request;
        this.generatorService = generatorService;
        this.imageService = imageService;
    }

    @Override
    public ResponseEntity<List<String>> getGenerator() {
        return new ResponseEntity<>(generatorService.getModels(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> postGenerator(String prompt, String antiPrompt, String modelName, Optional<Integer> seed) {
        int generationSeed = seed.orElse(random.nextInt());
        try {
            GenerationStatus status = generatorService.generate(prompt, antiPrompt, modelName, generationSeed);
            if (status == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(status.getUUID(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Image> generationStatus(String generationToken) {
        GenerationStatus status;
        try {
            status = generatorService.getGenerationStatus(generationToken).orElse(null);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (status == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseFromStatus(status);
    }

    private ResponseEntity<Image> responseFromStatus(GenerationStatus status) {
        return switch (status.getStage()) {
            case NOT_STARTED, IN_PROGRESS -> new ResponseEntity<>(HttpStatus.ACCEPTED);
            case SUCCESSFUL -> {
                Optional<Image> image = imageService.getImageById(status.getImageId());
                if (image.isEmpty()) {
                    yield new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                yield new ResponseEntity<>(image.get(), HttpStatus.CREATED);
            }
            case FAILED -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
