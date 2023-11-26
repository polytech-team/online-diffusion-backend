package team.polytech.online.diffusion.service.generator;

import team.polytech.online.diffusion.entity.GenerationStatus;

import java.util.List;
import java.util.Optional;

public interface GeneratorService {

    GenerationStatus generate(String prompt, String antiPrompt, String modelName, int seed);

    Optional<GenerationStatus> getGenerationStatus(String UUID);

    List<String> getModels();
}
