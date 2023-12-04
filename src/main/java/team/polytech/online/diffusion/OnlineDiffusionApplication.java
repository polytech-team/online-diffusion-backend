package team.polytech.online.diffusion;

import com.fasterxml.jackson.databind.Module;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(
    nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@ComponentScan(
    basePackages = {"team.polytech.online.diffusion", "team.polytech.online.diffusion.api" , "team.polytech.online.diffusion.config"},
    nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class OnlineDiffusionApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(OnlineDiffusionApplication.class, args);
    }

    @Override
    public void run(String... args) {
    }

    @Bean(name = "team.polytech.online.diffusion.OpenApiGeneratorApplication.jsonNullableModule")
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

}