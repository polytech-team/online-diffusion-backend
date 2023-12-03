package team.polytech.online.diffusion.repository;

import org.springframework.data.repository.CrudRepository;
import team.polytech.online.diffusion.entity.RegistrationToken;

public interface RegistrationTokenRepository extends CrudRepository<RegistrationToken, String> {
}
