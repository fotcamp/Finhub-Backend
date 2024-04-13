package fotcamp.finhub.admin.repository;

import fotcamp.finhub.common.domain.UserAvatar;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserAvatarRepository extends JpaRepository<UserAvatar, Long> {

}
