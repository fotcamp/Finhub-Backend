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
import org.springframework.http.HttpStatus;
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
        }else{
            topicRepository.delete(topic);
            return ResponseEntity.ok(ApiResponseWrapper.success());
        }
    }
}
