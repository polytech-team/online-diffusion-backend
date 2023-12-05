package team.polytech.online.diffusion.repository;

import org.springframework.data.repository.CrudRepository;
import team.polytech.online.diffusion.entity.RecoveryToken;

public interface RecoveryTokenRepository extends CrudRepository<RecoveryToken, String> {
}
