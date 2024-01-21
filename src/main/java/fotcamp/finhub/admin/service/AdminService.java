package fotcamp.finhub.admin.service;

import fotcamp.finhub.admin.domain.Manager;
import fotcamp.finhub.admin.dto.*;
import fotcamp.finhub.admin.repository.CategoryRepository;
import fotcamp.finhub.admin.repository.ManagerRepository;
import fotcamp.finhub.admin.repository.TopicRepository;
import fotcamp.finhub.admin.repository.UserTypeRepository;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.Category;
import fotcamp.finhub.common.domain.Topic;
import fotcamp.finhub.common.domain.UserType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminService {

    private final ManagerRepository managerRepository;
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final UserTypeRepository userTypeRepository;

    // 로그인
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> login(LoginDto loginDto) {
        try {
            Manager manager = managerRepository.findByUserId(loginDto.getId()).orElseThrow(EntityNotFoundException::new);
            if (manager.getPassword().equals(loginDto.getPassword())) {
                return ResponseEntity.ok(ApiResponseWrapper.success()); // 200
            }
            // 비밀번호 틀렸을 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseWrapper.fail("비밀번호 틀림"));
        } catch (EntityNotFoundException e) { // 유저 아이디가 틀린 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.fail("존재하지 않는 아이디"));
        }
    }

    // 카테고리 전체 조회
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        List<AllCategoryResponseDto> allCategoryResponseDtos = categories.stream().map(AllCategoryResponseDto::new).toList();
        return ResponseEntity.ok(ApiResponseWrapper.success(allCategoryResponseDtos));
    }

    // 카테고리 생성
    public ResponseEntity<ApiResponseWrapper> createCategory(CreateCategoryDto createCategoryDto) {
        try {
            // 중복 검사
            categoryRepository.findByName(createCategoryDto.getName()).ifPresent(e -> {
                throw new DuplicateKeyException("이미 존재하는 카테고리");
            });

            Category category = Category.builder()
                    .name(createCategoryDto.getName())
                    .build();

            categoryRepository.save(category);

            return ResponseEntity.ok(ApiResponseWrapper.success());
        } catch (DuplicateKeyException e) {
            log.error("이미 존재하는 카테고리입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseWrapper.fail("이미 존재하는 카테고리"));
        }

    }

    // 카테고리 수정 (보이기 / 숨기기)
    public ResponseEntity<ApiResponseWrapper> modifyCategory(ModifyCategoryDto modifyCategoryDto) {
        try {
            // 없는 카테고리면 예외
            Category category = categoryRepository.findById(modifyCategoryDto.getId()).orElseThrow(EntityNotFoundException::new);
            // 수정할 카테고리명 중복 검사
            categoryRepository.findByName(modifyCategoryDto.getName()).ifPresent(e -> {
                throw new DuplicateKeyException("이미 존재하는 카테고리");
            });
            // userYN값 Y, N인지 판단
            if (!("Y".equals(modifyCategoryDto.getUseYN()) || "N".equals(modifyCategoryDto.getUseYN()))) {
                throw new IllegalArgumentException();
            }
            category.modifyCategory(modifyCategoryDto.getName(), modifyCategoryDto.getUseYN());

            return ResponseEntity.ok(ApiResponseWrapper.success());
        } catch (EntityNotFoundException e) {
            log.error("존재하지 않는 카테고리입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.fail("존재하지 않는 카테고리"));
        } catch (DuplicateKeyException e) {
            log.error("이미 존재하는 카테고리명입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseWrapper.fail("이미 존재하는 카테고리명"));
        } catch (IllegalArgumentException e) {
            log.error("useYN에 다른 값이 들어왔습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail("Y, N 값 중 하나를 입력해주세요"));
        }
    }

    // 토픽 생성
    public ResponseEntity<ApiResponseWrapper> createTopic(CreateTopicDto createTopicDto) {
        try {
            Category topicCategory = categoryRepository.findById(createTopicDto.getCategoryId()).orElseThrow(EntityNotFoundException::new);

            Topic topic = Topic.builder()
                    .title(createTopicDto.getTitle())
                    .definition(createTopicDto.getDefinition())
                    .shortDefinition(createTopicDto.getShortDefinition())
                    .thumbnailImgPath(createTopicDto.getThumbnail())
                    .createdBy(createTopicDto.getCreatedBy())
                    .build();

            topic.setCategory(topicCategory);
            topicRepository.save(topic);

            return ResponseEntity.ok(ApiResponseWrapper.success());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.fail("존재하지 않는 카테고리"));
        }
    }

    // 유저 타입 생성
    public ResponseEntity<ApiResponseWrapper> createUserType(CreateUserTypeDto createUserTypeDto) {
        try {
            // 중복 검사
            userTypeRepository.findByName(createUserTypeDto.getName()).ifPresent(e -> {
                throw new DuplicateKeyException("중복된 유저 타입");
            });

            UserType userType = UserType.builder()
                    .name(createUserTypeDto.getName())
                    .avatarImgPath(createUserTypeDto.getAvatar())
                    .build();

            userTypeRepository.save(userType);

            return ResponseEntity.ok(ApiResponseWrapper.success());
        } catch (DuplicateKeyException e) {
            log.error("이미 존재하는 유저 타입입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseWrapper.fail("이미 존재하는 유저타입"));
        }
    }
}
