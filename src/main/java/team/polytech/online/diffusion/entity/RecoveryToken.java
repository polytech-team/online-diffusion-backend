package team.polytech.online.diffusion.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash
public class RecoveryToken implements Serializable {
    @Id
    private String UUID;
    private Long userId;
    private Integer code;
    private Stage stage;
    private Integer triesLeft;

    public RecoveryToken() {
    }

    public RecoveryToken(String UUID, Long userId, Integer code, Stage stage, Integer triesLeft) {
        this.UUID = UUID;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    public Integer getCode() {
        return code;
    }

    public enum Stage {
        NOT_CONFIRMED, READY, USED
    }
}
