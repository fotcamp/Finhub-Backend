package fotcamp.finhub.admin.repository;

import fotcamp.finhub.admin.domain.ManagerRefreshToken;
import fotcamp.finhub.common.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ManagerRefreshRepository extends JpaRepository<ManagerRefreshToken, String> {

    ManagerRefreshToken findByEmail(String email);
}
