package team.polytech.onlinediffusionbackend.model;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

@RedisHash
public class Tokens implements Serializable {
    private final Long id;
    private final String accessToken;
    private final String refreshToken;

    public Tokens(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getUserId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
