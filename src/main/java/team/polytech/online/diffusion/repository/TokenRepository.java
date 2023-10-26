package team.polytech.online.diffusion.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import team.polytech.online.diffusion.entity.Tokens;

@Repository
public interface TokenRepository extends CrudRepository<Tokens, Long> {
    Optional<Tokens> findById(Long id);
}
