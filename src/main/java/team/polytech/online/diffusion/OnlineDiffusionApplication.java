package team.polytech.online.diffusion;

import com.fasterxml.jackson.databind.Module;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import team.polytech.online.diffusion.entity.User;
import team.polytech.online.diffusion.repository.UserRepository;

@SpringBootApplication(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@ComponentScan(
        basePackages = {"team.polytech.online.diffusion", "team.polytech.online.diffusion.api", "team.polytech.online.diffusion.config"},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class OnlineDiffusionApplication implements CommandLineRunner {
    private final UserRepository userRepository;

    public OnlineDiffusionApplication(UserRepository repository) {
        this.userRepository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(OnlineDiffusionApplication.class, args);
    }

    @Override
    public void run(String... args) {
        User user = new User();
        user.setUsername("user");
        user.setEmail("foo@gmail.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        userRepository.save(user);
    }

    @Bean(name = "team.polytech.online.diffusion.OpenApiGeneratorApplication.jsonNullableModule")
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

}