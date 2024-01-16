package fotcamp.finhub.admin.repository;

import fotcamp.finhub.admin.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByUserId(String userId);
}
