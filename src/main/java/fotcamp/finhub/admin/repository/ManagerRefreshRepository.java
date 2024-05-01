package fotcamp.finhub.admin.repository;

import fotcamp.finhub.admin.domain.Manager;
import fotcamp.finhub.admin.domain.ManagerRefreshToken;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ManagerRefreshRepository extends JpaRepository<ManagerRefreshToken, String> {

    Optional<ManagerRefreshToken> findByEmail(String email);
}
