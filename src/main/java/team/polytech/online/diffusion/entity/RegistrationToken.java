package team.polytech.online.diffusion.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash
public class RegistrationToken implements Serializable {
    @Id
    private String UUID;
    private Long userId;
    private User.Status status;

    public RegistrationToken() {
    }

    public RegistrationToken(String UUID, Long userId, User.Status status) {
        this.UUID = UUID;
        this.userId = userId;
        this.status = status;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public User.Status getStatus() {
        return status;
    }

    public void setStatus(User.Status status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
