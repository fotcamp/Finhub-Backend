package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByEmail(String email);
}
