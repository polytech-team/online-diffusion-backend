package team.polytech.online.diffusion.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash
public class GenerationStatus implements Serializable {
    @Id
    private String UUID;
    private Stage stage;
    private Long imageId;

    public GenerationStatus() {
    }

    public GenerationStatus(String UUID, Stage stage) {
        this(UUID, stage, null);
    }

    public GenerationStatus(String UUID, Stage stage, Long imageId) {
        this.UUID = UUID;
        this.stage = stage;
        this.imageId = imageId;
    }

    public String getUUID() {
        return UUID;
    }

    public Stage getStage() {
        return stage;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public enum Stage {
        NOT_STARTED, IN_PROGRESS, SUCCESSFUL, FAILED
    }
}
