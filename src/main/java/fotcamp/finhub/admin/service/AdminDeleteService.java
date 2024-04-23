package fotcamp.finhub.admin.service;


import fotcamp.finhub.admin.dto.request.DeleteCategoryRequestDto;
import fotcamp.finhub.admin.dto.request.DeleteTopicRequestDto;
import fotcamp.finhub.admin.repository.*;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.*;
import fotcamp.finhub.main.repository.MemberScrapRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ResponseEntity<ApiResponseWrapper> deleteCategory(DeleteCategoryRequestDto dto){
        /** 코드 다 버려야됨 */
        Category category = categoryRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("카테고리 아이디가 존재하지 않습니다."));
        for (Topic topic : category.getTopics()) {
            topic.removeCategory();
            topicRepository.save(topic); // 변경 사항 저장
        }
        categoryRepository.delete(category);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> deleteTopic(DeleteTopicRequestDto dto){

        /** 코드 다 버려야됨*/

        Topic topic = topicRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("토픽 아이디가 존재하지 않습니다."));

        // 연관된 GPT 테이블 참조 해제
        List<Gpt> gptList = gptRepository.findByTopic(topic);
        for (Gpt gpt : gptList){
            gpt.removeTopic();
            gptRepository.save(gpt);
        }

        // 연관된 TOPIC SCRAP 테이블 참조 해제
        List<MemberScrap> memberScrapList = memberScrapRepository.findByTopic(topic);
        for (MemberScrap memberScrap : memberScrapList){
            memberScrap.removeTopic();
            memberScrapRepository.save(memberScrap);
        }

        // TOPIC GPT COLUMN 테이블 참조 해제
        List<TopicGptColumn> topicGptColumnList = topicGptColumnRepository.findByTopic(topic);
        for (TopicGptColumn topicGptColumn : topicGptColumnList){
            topicGptColumn.removeTopic();
            topicGptColumnRepository.save(topicGptColumn);
        }

        // TOPIC QUIZ 테이블 참조 해제
        List<TopicQuiz> topicQuizList = topicQuizRepository.findByTopic(topic);
        for (TopicQuiz topicQuiz : topicQuizList){
            topicQuiz.removeTopic();
            topicQuizRepository.save(topicQuiz);
        }

        // CATEGORY 테이블 참조 해제
        topic.getCategory().getTopics().remove(topic);

        topicRepository.delete(topic);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }
}
