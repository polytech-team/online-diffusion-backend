package team.polytech.online.diffusion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import team.polytech.automatic.webui.api.DefaultApi;
import team.polytech.automatic.webui.invoker.ApiClient;
import team.polytech.automatic.webui.model.SDModelItem;
import team.polytech.automatic.webui.model.StableDiffusionProcessingTxt2Img;
import team.polytech.automatic.webui.model.TextToImageResponse;

@Service
public class GeneratorServiceImpl implements GeneratorService  {

    DefaultApi automaticUiApi;

    @Autowired
    public GeneratorServiceImpl(ApiClient client) {
        automaticUiApi = new DefaultApi(client);
    }

    @Override
    public String generate(String prompt, String antiPrompt, String modelName, long seed) {
        StableDiffusionProcessingTxt2Img request = new StableDiffusionProcessingTxt2Img();
        request.prompt(prompt);
        request.negativePrompt(antiPrompt);
        request.seed((int) seed);
        request.height(128);
        request.width(128);
        request.steps(5);
        request.batchSize(1);
        TextToImageResponse response = automaticUiApi.text2imgapiSdapiV1Txt2imgPost(request);
        return response.getImages().get(0);
    }

    @Override
    public List<String> getModels() {
        return automaticUiApi.getSdModelsSdapiV1SdModelsGet().stream().map(SDModelItem::getModelName).toList();
    }
}
