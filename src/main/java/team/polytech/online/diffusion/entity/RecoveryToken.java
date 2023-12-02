package team.polytech.online.diffusion.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash
public class RecoveryToken implements Serializable {
    @Id
    private String UUID;
    private Long code;
    private Stage stage;
    private Integer triesLeft;

    public RecoveryToken() {
    }

    public RecoveryToken(String UUID, Long code, Stage stage) {
        this(UUID, code, stage, 3);
    }

    public RecoveryToken(String UUID, Long code, Stage stage, Integer triesLeft) {
        this.UUID = UUID;
        this.code = code;
        this.stage = stage;
        this.triesLeft = triesLeft;
    }

    public String getUUID() {
        return UUID;
    }

    public Stage getStage() {
        return stage;
    }

    public Integer getTriesLeft() {
        return triesLeft;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTriesLeft(Integer triesLeft) {
        this.triesLeft = triesLeft;
    }

    public enum Stage {
        NOT_CONFIRMED, READY, USED
    }
}
