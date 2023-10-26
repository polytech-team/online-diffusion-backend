package team.polytech.online.diffusion.config;

import team.polytech.online.diffusion.model.InvalidData;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class EnumConverterConfiguration {

    @Bean(name = "team.polytech.online.diffusion.config.EnumConverterConfiguration.invalidDataConverter")
    Converter<String, InvalidData> invalidDataConverter() {
        return new Converter<String, InvalidData>() {
            @Override
            public InvalidData convert(String source) {
                return InvalidData.fromValue(source);
            }
        };
    }

}
