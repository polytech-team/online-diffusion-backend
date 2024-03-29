package team.polytech.online.diffusion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.polytech.automatic.webui.api.DefaultApi;
import team.polytech.automatic.webui.invoker.ApiClient;
import team.polytech.imgur.api.ImageApi;
import team.polytech.imgur.invoker.ImgurApiClient;
import team.polytech.imgur.invoker.auth.ApiKeyAuth;

@Configuration
public class ApplicationConfig {
    private final UserDetailsService userDetailsService;

    @Value("${imgur.clientId}")
    private String clientId;
    @Value("${stable-diffusion.url}")
    private String urlSD;
    @Value("${stable-diffusion.username}")
    private String userSD;
    @Value("${stable-diffusion.password}")
    private String passwordSD;

    @Autowired
    public ApplicationConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ApiClient getClient() {
        ApiClient client = new ApiClient();
        client.setBasePath(urlSD);
        client.setUsername(userSD);
        client.setPassword(passwordSD);
        return client;
    }

    @Bean
    DefaultApi getAutomaticApi(ApiClient client) {
        return new DefaultApi(client);
    }

    @Bean
    ImgurApiClient getImgurApi() {
        ImgurApiClient anonymous = new ImgurApiClient(true);
        anonymous.setBasePath("https://api.imgur.com");

        ApiKeyAuth client = (ApiKeyAuth) anonymous.getAuthentication("clientId");
        client.setApiKey("Client-ID " + clientId);
        return anonymous;
    }

    @Bean
    ImageApi getImageApi(ImgurApiClient client) {
        return new ImageApi(client);
    }
}