package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    Optional<UserType> findByName(String name);

//    @Query("SELECT m FROM UserType m ORDER BY m.id ")
//    List<UserType> findAllJobListOrderById();

    List<UserType> findAllByUseYNOrderByIdAsc(String useYN);
    Page<UserType> findFirstByOrderByIdAsc(Pageable pageable);

}
