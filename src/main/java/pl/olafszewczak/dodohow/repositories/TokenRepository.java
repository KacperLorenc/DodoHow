package pl.olafszewczak.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.olafszewczak.dodohow.security.VerificationToken;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
