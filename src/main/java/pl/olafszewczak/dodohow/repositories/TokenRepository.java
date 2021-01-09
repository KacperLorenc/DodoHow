package pl.olafszewczak.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.olafszewczak.dodohow.security.VerificationToken;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
