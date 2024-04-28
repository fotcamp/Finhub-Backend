package fotcamp.finhub.main.repository;


import fotcamp.finhub.common.domain.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository  extends JpaRepository<Announcement, Long> {

    @Query("SELECT a FROM Announcement a WHERE a.id <= :cursorId ORDER BY a.createdTime DESC")
    List<Announcement> find7Announcement(@Param(value = "cursorId") Long cursorId, Pageable pageable);
    // 이전 공지사항들을 불러와야해서 cursorId보다 작은 7개를 가져와야함. (size = 7)

    @Query("SELECT a FROM Announcement a ORDER BY a.createdTime DESC")
    Page<Announcement> findOrderByTime(Pageable pageable);
}
