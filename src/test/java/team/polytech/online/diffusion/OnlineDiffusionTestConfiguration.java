package team.polytech.online.diffusion;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration
public class OnlineDiffusionTestConfiguration {
    @Bean
    public JavaMailSender getSender() {
        return Mockito.mock(JavaMailSender.class);
    }

    @Bean
    ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(new MockConnectionFactory());
    }
}
