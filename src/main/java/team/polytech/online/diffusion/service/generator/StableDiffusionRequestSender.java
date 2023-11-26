package team.polytech.online.diffusion.service.generator;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import team.polytech.online.diffusion.config.SDQueueCfg;
import team.polytech.online.diffusion.entity.SDTxt2ImgRequest;

@Service
public class StableDiffusionRequestSender {
    private final RabbitTemplate rabbitTemplate;

    public StableDiffusionRequestSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendRequest(SDTxt2ImgRequest request) {
        rabbitTemplate.convertAndSend(SDQueueCfg.ROUTING_KEY, request);
    }

}
