package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.RecentSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {

    @Query("SELECT r FROM RecentSearch r WHERE r.member.id = :memberId ORDER BY r.localDateTime DESC")
    List<RecentSearch> findByMember_IdOrderByLocalDateTimeDesc(@Param("memberId") Long memberId, Pageable pageable);

    Optional<RecentSearch> findByMember_memberIdAndKeyword(Long memberId, String keyword);
    void deleteByMember_memberId(Long memberId);
}
