package team.polytech.online.diffusion.repository;

import org.springframework.data.repository.CrudRepository;
import team.polytech.online.diffusion.entity.GenerationStatus;

public interface GenerationStatusRepository extends CrudRepository<GenerationStatus, String> {
}
