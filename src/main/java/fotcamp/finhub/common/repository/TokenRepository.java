package fotcamp.finhub.common.repository;

import fotcamp.finhub.common.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByAccountEmail(String email);
}
