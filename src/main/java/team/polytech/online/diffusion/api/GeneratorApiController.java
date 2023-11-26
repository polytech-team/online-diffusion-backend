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

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-10-26T02:19:33.552470+03:00[Europe/Moscow]")
@Controller
@RequestMapping("${openapi.onlineDiffusion.base-path:}")
public class GeneratorApiController implements GeneratorApi {

    private final NativeWebRequest request;
    private final GeneratorService generatorService;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    @Autowired
    public GeneratorApiController(NativeWebRequest request,
                                  GeneratorService generatorService) {
        this.request = request;
        this.generatorService = generatorService;
    }

    @Override
    public ResponseEntity<List<String>> getGenerator() {
        return new ResponseEntity<>(generatorService.getModels(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> postGenerator(String prompt, String antiPrompt, String modelName, Optional<Long> seed) {
        long generationSeed = seed.orElse(random.nextLong());
        try {
            return new ResponseEntity<>(generatorService.generate(prompt, antiPrompt, modelName, generationSeed).getUUID(), HttpStatus.OK);
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
            case SUCCESSFUL -> new ResponseEntity<>(HttpStatus.CREATED);
            case FAILED -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
