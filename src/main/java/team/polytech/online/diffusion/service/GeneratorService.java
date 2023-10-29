package team.polytech.online.diffusion.service;

import java.util.List;
import java.util.Optional;

public interface GeneratorService {

    //TODO: нормальный метод, это тупо всратый тест работы либы
    String generate(String prompt, String antiPrompt, String modelName, long seed);

    List<String> getModels();
}
