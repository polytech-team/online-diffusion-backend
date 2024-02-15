package team.polytech.online.diffusion.service.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import team.polytech.automatic.webui.model.StableDiffusionProcessingTxt2Img;
import team.polytech.imgur.api.ImageApi;
import team.polytech.imgur.model.ImageResponse;
import team.polytech.online.diffusion.entity.GenerationStatus;
import team.polytech.online.diffusion.entity.ImageEntity;
import team.polytech.online.diffusion.entity.SDTxt2ImgRequest;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.repository.GenerationStatusRepository;
import team.polytech.online.diffusion.repository.ImageRepository;
import team.polytech.online.diffusion.repository.UserRepository;

import java.util.Optional;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class ImgurImageUploadService {
    private static final Logger LOG = LoggerFactory.getLogger(ImgurImageUploadService.class);

    private final ThreadPoolExecutor imageThreadPool;
    private final GenerationStatusRepository generationRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ImageApi imageApi;

    public ImgurImageUploadService(ImageApi imgurApi,
                                   GenerationStatusRepository generationRepository,
                                   ImageRepository imageRepository,
                                   UserRepository userRepository) {
        this.imageApi = imgurApi;
        this.generationRepository = generationRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.imageThreadPool = new ThreadPoolExecutor(0, Runtime.getRuntime().availableProcessors(),
                60L, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    public void imageUploadTask(SDTxt2ImgRequest imageRequest, String base64Image) {
        StableDiffusionProcessingTxt2Img request = imageRequest.getRequest();
        imageThreadPool.execute(new ImageUploadTask(imageRequest.getUUID(),
                request.getPrompt(), request.getNegativePrompt(),
                request.getSeed(), imageRequest.getModel(), imageRequest.getUserId(), base64Image));
    }

    private class ImageUploadTask implements Runnable {

        private final String UUID;
        private final String prompt;
        private final String antiPrompt;
        private final Integer seed;
        private final String model;
        private final long authorId;
        private final String base64Image;

        public ImageUploadTask(String UUID, String prompt, String antiPrompt,
                               Integer seed, String model, long authorId, String base64Image) {
            this.UUID = UUID;
            this.prompt = prompt;
            this.antiPrompt = antiPrompt;
            this.seed = seed;
            this.model = model;
            this.authorId = authorId;
            this.base64Image = base64Image;
        }

        @Override
        public void run() {
            Optional<User> author = userRepository.findById(authorId);

            if (author.isEmpty()) {
                LOG.error("Author was not found in user repository: " + authorId);
                generationRepository.save(new GenerationStatus(UUID, GenerationStatus.Stage.FAILED));
                return;
            }

            ImageResponse response;
            try {
                response = imageApi.uploadImage(base64Image, null, prompt, UUID, UUID);
            } catch (RestClientException restClientException) {
                LOG.error("Execution failed for imgur api request ", restClientException);
                generationRepository.save(new GenerationStatus(UUID, GenerationStatus.Stage.FAILED));
                return;
            }

            if (!response.getSuccess() || response.getData().getLink().isEmpty()) {
                LOG.warn("Request was successful, yet image upload failed: " + response);
                generationRepository.save(new GenerationStatus(UUID, GenerationStatus.Stage.FAILED));
                return;
            }

            ImageEntity image = new ImageEntity();
            image.setURL(response.getData().getLink());
            image.setPrompt(prompt);
            image.setAntiPrompt(antiPrompt);
            image.setSeed(seed);
            image.setUser(author.get());
            image.setPublicity(ImageEntity.Publicity.UNUSED);
            image.setModel(model);

            ImageEntity result = imageRepository.save(image);
            generationRepository.save(new GenerationStatus(UUID, GenerationStatus.Stage.SUCCESSFUL, result.getId()));
            LOG.info("Image upload successful! " + response.getData().getLink());

            User user = author.get();
            user.incrementGenerated();
            userRepository.save(user);
        }
    }
}
