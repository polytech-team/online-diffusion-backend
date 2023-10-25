package team.polytech.onlinediffusionbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import team.polytech.onlinediffusionbackend.model.User;
import team.polytech.onlinediffusionbackend.repository.UserRepository;

@SpringBootApplication
public class OnlineDiffusionBackendApplication implements CommandLineRunner {
	private final UserRepository userRepository;

	public OnlineDiffusionBackendApplication(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(OnlineDiffusionBackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		User user = new User();
		user.setUsername("user");
		user.setPassword(new BCryptPasswordEncoder().encode("password"));
		userRepository.save(user);
	}
}
