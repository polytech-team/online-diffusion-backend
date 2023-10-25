package team.polytech.onlinediffusionbackend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import team.polytech.onlinediffusionbackend.model.Tokens;

@Repository
public interface TokenRepository extends CrudRepository<Tokens, Long> {
    Optional<Tokens> findById(Long id);
}
