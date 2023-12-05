package team.polytech.online.diffusion.service.auth;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import team.polytech.online.diffusion.entity.AuthToken;
import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.model.AuthInfo;
import team.polytech.online.diffusion.repository.AuthTokenRepository;

@Service
public class JwtService {
    private final long jwtExpiration = 86400000;
    private final long refreshExpiration = 604800000;
    private final SecretKey secretAccessKey = Keys.hmacShaKeyFor("secretAccessKey12321321312213213213212132".getBytes());
    private final SecretKey secretRefreshKey = Keys.hmacShaKeyFor("secretRefreshKey123213213213213213213123213213".getBytes());

    private final AuthTokenRepository authTokenRepository;

    public JwtService(AuthTokenRepository authTokenRepository) {
        this.authTokenRepository = authTokenRepository;
    }

    private Claims extractClaims(String token, SecretKey secretKey) {
        return Jwts
            .parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public AuthInfo generateAuthInfo(User user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        authTokenRepository.save(new AuthToken(user.getId(), accessToken, refreshToken));
        return new AuthInfo(refreshToken, accessToken);
    }

    public String generateAccessToken(User user) {
        return generateToken(user, secretAccessKey, jwtExpiration);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, secretRefreshKey, refreshExpiration);
    }

    private String generateToken(User user, SecretKey secretKey, long expiration) {
        Date now = new Date();
        return Jwts
            .builder()
            .subject(String.valueOf(user.getId()))
            .issuedAt(now)
            .expiration(new Date(now.getTime() + expiration))
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact();
    }

    public Long getUserIdFromAccess(String accessToken) {
        Claims claims = extractClaims(accessToken, secretAccessKey);
        if (claims.getExpiration().before(new Date())) {
            return null;
        }
        Optional<AuthToken> tokens = authTokenRepository.findById(Long.valueOf(claims.getSubject()));
        return tokens.isPresent() && Objects.equals(tokens.get().getAccessToken(), accessToken)
            ? tokens.get().getUserId()
            : null;
    }

    public Long getUserIdFromRefresh(String refreshToken) {
        try {
            Claims claims = extractClaims(refreshToken, secretRefreshKey);
            if (claims.getExpiration().before(new Date())) {
                return null;
            }
            Optional<AuthToken> tokens = authTokenRepository.findById(Long.valueOf(claims.getSubject()));
            return tokens.isPresent() && Objects.equals(tokens.get().getRefreshToken(), refreshToken)
                ? tokens.get().getUserId()
                : null;
        } catch (Exception exception) {
            return null;
        }
    }
}
