package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.MemberAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgreementRepository extends JpaRepository<MemberAgreement, Long> {

    MemberAgreement findByMember(Member member);

    @Query("SELECT ma.member FROM MemberAgreement ma WHERE ma.pushYn = :pushYn")
    List<Member> findMembersByPushYn(@Param("pushYn") boolean pushYn);

    @Query("SELECT ma.member FROM MemberAgreement ma WHERE ma.pushYn = true AND ma.member.email IN :emails")
    List<Member> findMembersByPushYnTrueAndEmails(@Param("emails") List<String> emails);


}
