package fotcamp.finhub.admin.repository;

import fotcamp.finhub.admin.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByEmail(String email);
    Optional<Manager> findByManagerUuid(String uuid);

    @Query("SELECT m FROM Manager m WHERE m.email IN :emails")
    List<Manager> findByPushYnAndEmails(@Param("emails") List<String> emails);
}
