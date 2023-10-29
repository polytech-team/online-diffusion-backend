package team.polytech.online.diffusion.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import jakarta.annotation.Generated;
import team.polytech.online.diffusion.service.GeneratorService;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-10-26T02:19:33.552470+03:00[Europe/Moscow]")
@Controller
@RequestMapping("${openapi.onlineDiffusion.base-path:}")
public class GeneratorApiController implements GeneratorApi {

    private final NativeWebRequest request;
    private final GeneratorService generatorService;
    private final Random random = new Random(System.currentTimeMillis());

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
        long seedl = seed.orElse(random.nextLong());
        return new ResponseEntity<>(generatorService.generate(prompt, antiPrompt, modelName, seedl), HttpStatus.OK);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
