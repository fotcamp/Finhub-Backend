package fotcamp.finhub.admin.service;


import com.google.protobuf.Api;
import fotcamp.finhub.admin.dto.request.*;
import fotcamp.finhub.admin.repository.*;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.*;
import fotcamp.finhub.main.repository.MemberRepository;
import fotcamp.finhub.main.repository.MemberScrapRepository;
import fotcamp.finhub.main.repository.ReportReasonsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminDeleteService {

    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final GptRepository gptRepository;
    private final MemberScrapRepository memberScrapRepository;
    private final TopicGptColumnRepository topicGptColumnRepository;
    private final TopicQuizRepository topicQuizRepository;
    private final MemberQuizRepository memberQuizRepository;
    private final UserAvatarRepository userAvatarRepository;
    private final UserTypeRepository userTypeRepository;
    private final MemberRepository memberRepository;
    private final QuizRepository quizRepository;
    private final GptColumnRepository gptColumnRepository;
    private final CalendarEmoticonRepository calendarEmoticonRepository;
    private final ReportReasonsRepository reportReasonsRepository;
    private final BannerRepository bannerRepository;

    public ResponseEntity<ApiResponseWrapper> deleteCategory(DeleteCategoryRequestDto dto){

        Category category = categoryRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("카테고리 아이디가 존재하지 않습니다."));
        long count = topicRepository.countTopicsById(category.getId());

        if (count == 0L){
            categoryRepository.delete(category);
            return ResponseEntity.ok(ApiResponseWrapper.success());
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail("해당 카테고리에 토픽이 존재합니다."));
        }
    }

    public ResponseEntity<ApiResponseWrapper> deleteTopic(DeleteTopicRequestDto dto){

        Topic topic = topicRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("토픽 아이디가 존재하지 않습니다."));

        if (gptRepository.countGptListById(dto.getId()) != 0L){
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("Gpt 테이블에 연관된 토픽이 있습니다."));
        } else if (memberScrapRepository.countMemberScrapByTopicId(dto.getId()) != 0L) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("MemberScrap 테이블에 연관된 토픽이 있습니다."));
        } else if (topicGptColumnRepository.countTopicGptColumnByTopicId(dto.getId())!= 0L) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("TopicGptColumn 테이블에 연관된 토픽이 있습니다."));
        } else if(topicQuizRepository.countTopicQuizByTopic(topic.getId()) != 0L){
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("TopicQuiz 테이블에 연관된 토픽이 있습니다."));
        }else{
            topicRepository.delete(topic);
            return ResponseEntity.ok(ApiResponseWrapper.success());
        }
    }

    public ResponseEntity<ApiResponseWrapper> deleteUsertype(DeleteUsertypeRequestDto dto){
        UserType userType = userTypeRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("id가 존재하지 않습니다."));
        long count = memberRepository.countMemberUsingUsertype(userType);
        if (count != 0){
            // 해당 유저타입을 사용중인 멤버들을 찾아서 전부 null처리
            List<Member> memberList = memberRepository.findMemberListUsingUsertype(userType);
            for ( Member member : memberList){
                member.removeUsertype();
            }
            memberRepository.saveAll(memberList);
        }
        userTypeRepository.delete(userType);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> deleteAvatar(DeleteAvatarRequestDto dto){
        UserAvatar userAvatar = userAvatarRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("id가 존재하지 않습니다."));
        long count = memberRepository.countMemberUsingUserAvatar(userAvatar);
        if (count != 0){
            List<Member> memberList = memberRepository.findMemberListUsingUserAvatar(userAvatar);
            for (Member member : memberList){
                member.removeUserAvatar();
            }
            memberRepository.saveAll(memberList);
        }
        userAvatarRepository.delete(userAvatar);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> deleteQuiz(DeleteQuizRequestDto dto){
        Quiz quiz = quizRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("id가 존재하지 않습니다."));
        // 퀴즈 삭제하면 토픽퀴즈, 멤버퀴즈 테이블 정보들도 제거.
        List<TopicQuiz> topicQuizList = topicQuizRepository.findByQuiz(quiz);
        if (!topicQuizList.isEmpty()){
            topicQuizRepository.deleteAll(topicQuizList);
        }
        List<MemberQuiz> memberQuizList = memberQuizRepository.findByQuiz(quiz);
        if (!memberQuizList.isEmpty()) {
            Iterator<MemberQuiz> iterator = memberQuizList.iterator();
            while (iterator.hasNext()){
                MemberQuiz memberQuiz = iterator.next();
                memberQuiz.getMember().getQuizList().remove(memberQuiz);
                iterator.remove();
            }
            memberQuizRepository.deleteAll(memberQuizList);
        }
        quizRepository.delete(quiz);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> deleteColumn(DeleteColumnRequestDto dto){
        GptColumn gptColumn = gptColumnRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("id가 존재하지 않습니다."));
        List<TopicGptColumn> topicGptColumnList = topicGptColumnRepository.findByGptColumn(gptColumn);
        if (!topicGptColumnList.isEmpty()){
            topicGptColumnRepository.deleteByGptColumn(gptColumn);
        }
        gptColumnRepository.delete(gptColumn);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> deleteEmoji(DeleteCalendarEmojiRequestDto dto){
        CalendarEmoticon calendarEmoticon = calendarEmoticonRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 ID"));
        // 이모티콘존재 확인 -> 사용중인 member 찾아서 전부 null로 교체 -> 이모티콘삭제
        List<Member> memberListusingEmoji = memberRepository.findByCalendarEmoticon(calendarEmoticon);
        for (Member member : memberListusingEmoji){
            member.removeCalendarEmoticon();
            memberRepository.save(member);
        }
        calendarEmoticonRepository.delete(calendarEmoticon);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> deleteReportReason(DeleteReportReasonRequestDto dto){
        ReportReasons reportReasons = reportReasonsRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("id가 존재하지 않습니다."));
        reportReasonsRepository.deleteById(reportReasons.getId());
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> deleteBanner(DeleteBannerRequestDto dto){
        Banner banner = bannerRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("id가 존재하지 않습니다."));
        bannerRepository.delete(banner);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }
}
