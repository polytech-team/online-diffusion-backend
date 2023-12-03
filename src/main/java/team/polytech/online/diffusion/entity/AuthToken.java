package team.polytech.online.diffusion.entity;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash
public class AuthToken implements Serializable {
    private final Long id;
    private final String accessToken;
    private final String refreshToken;

    public AuthToken(Long id, String accessToken, String refreshToken) {
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
