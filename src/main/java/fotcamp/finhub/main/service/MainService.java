package fotcamp.finhub.main.service;

import fotcamp.finhub.admin.repository.CategoryRepository;
import fotcamp.finhub.admin.repository.TopicRepository;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.Category;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.Topic;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.process.CategoryListProcessDto;
import fotcamp.finhub.main.dto.process.TopicListProcessDto;
import fotcamp.finhub.main.dto.request.ChangeNicknameRequestDto;
import fotcamp.finhub.main.dto.process.SearchResultListProcessDto;
import fotcamp.finhub.main.dto.response.LoginHomeResponseDto;
import fotcamp.finhub.main.dto.response.SearchResponseDto;
import fotcamp.finhub.main.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class MainService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> home(int size){
        // 로그인 비로그인 체크 검사 필요
        // 전체 카테고리리스트
        List<Category> allCategories = categoryRepository.findAllByOrderByIdAsc();
        List<CategoryListProcessDto> categoryListDtos = allCategories.stream()
                .map(category -> new CategoryListProcessDto(category.getId(), category.getName())).collect(Collectors.toList());
        // 첫번째 카테고리의 토픽 7개
        Category firstCategory = categoryRepository.findFirstByOrderByIdAsc();
        List<Topic> topicTop7 = topicRepository.findByCategoryAndIdGreaterThan(firstCategory, 0L, PageRequest.of(0, size));

        List<TopicListProcessDto> topicListDtos = topicTop7.stream()
                .map(topic -> new TopicListProcessDto(topic.getId(), topic.getTitle(), topic.getSummary())).collect(Collectors.toList());
        LoginHomeResponseDto loginHomeResponseDto = new LoginHomeResponseDto(categoryListDtos, topicListDtos);
        return ResponseEntity.ok(ApiResponseWrapper.success(loginHomeResponseDto));
    }
    
    public ResponseEntity<ApiResponseWrapper> changeNickname(CustomUserDetails userDetails , ChangeNicknameRequestDto dto){

        try{
            String newNickname = dto.getNewNickname();
            if (newNickname.length()>= 2 && newNickname.length() <= 10){
                Long memberId = userDetails.getMemberIdasLong();
                Member existingMember = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
                existingMember.updateNickname(newNickname);
                memberRepository.save(existingMember);
                return ResponseEntity.ok(ApiResponseWrapper.success("변경 완료"));
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail("변경조건에 맞게 작성하세요."));
            }
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.fail("해당 요청 데이터가 존재하지 않습니다."));
        }
    }

    public ResponseEntity<ApiResponseWrapper> membershipResign(CustomUserDetails userDetails){

        try{
            Long memberId = userDetails.getMemberIdasLong();
            Member existingMember = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
            memberRepository.delete(existingMember);
            return ResponseEntity.ok(ApiResponseWrapper.success("탈퇴 완료"));
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.fail("해당 요청 데이터가 존재하지 않습니다."));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> search(String method, String keyword, int pageSize, int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Topic> pageResult = null;
        switch (method) {
            case "title" -> pageResult = topicRepository.findByTitleContaining(keyword, pageable);
            case "summary" -> pageResult = topicRepository.findBySummaryContaining(keyword, pageable);
            case "both" -> pageResult = topicRepository.findByTitleContainingOrSummaryContaining(keyword,keyword, pageable);
            default -> throw new IllegalArgumentException("검색방법이 잘못되었습니다.");
        }

        List<Topic> resultList = pageResult.getContent();
        List<SearchResultListProcessDto> response = resultList.stream()
                .map(topic -> new SearchResultListProcessDto(topic.getTitle(), topic.getSummary())).collect(Collectors.toList());
        SearchResponseDto responseDto = new SearchResponseDto(response, page, pageResult.getTotalPages(), pageResult.getTotalElements());
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> otherCategories(Long categoryId, int size){
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException("카테고리가 존재하지 않습니다.")
        );
        List<Topic> topicList = topicRepository.findByCategoryAndIdGreaterThan(findCategory, 0L, PageRequest.of(0, size));
        //토픽만 7개
        Stream<TopicListProcessDto> topicListDtos = topicList.stream()
                .map(topic -> new TopicListProcessDto(topic.getId(), topic.getTitle(), topic.getSummary()));
        return ResponseEntity.ok(ApiResponseWrapper.success(topicListDtos));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> more(Long categoryId, Long cursorId, int size){
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException("카테고리가 존재하지 않습니다.")
        );
        List<Topic> topicList = topicRepository.findByCategoryAndIdGreaterThan(findCategory, cursorId, PageRequest.of(0, size));
        //토픽만 7개
        Stream<TopicListProcessDto> topicListDtos = topicList.stream()
                .map(topic -> new TopicListProcessDto(topic.getId(), topic.getTitle(), topic.getSummary()));
        return ResponseEntity.ok(ApiResponseWrapper.success(topicListDtos));
    }
}