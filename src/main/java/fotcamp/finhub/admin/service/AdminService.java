package fotcamp.finhub.admin.service;

import fotcamp.finhub.admin.domain.Manager;
import fotcamp.finhub.admin.dto.CreateCategoryDto;
import fotcamp.finhub.admin.dto.LoginDto;
import fotcamp.finhub.admin.dto.ModifyCategoryDto;
import fotcamp.finhub.admin.repository.CategoryRepository;
import fotcamp.finhub.admin.repository.ManagerRepository;
import fotcamp.finhub.common.domain.Category;
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

    // 로그인
    @Transactional(readOnly = true)
    public ResponseEntity<Void> login(LoginDto loginDto) {
        try {
            Manager manager = managerRepository.findByUserId(loginDto.getId()).orElseThrow(EntityNotFoundException::new);
            if (manager.getPassword().equals(loginDto.getPassword())) {
                return ResponseEntity.ok().build(); // 200
            }
            // 비밀번호 틀렸을 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        } catch (EntityNotFoundException e) { // 유저 아이디가 틀린 경우
            return ResponseEntity.notFound().build(); // 404
        }
    }

    // 카테고리 전체 조회
    @Transactional(readOnly = true)
    public ResponseEntity<List<Category>> getAllCategory() {
        return ResponseEntity.ok(categoryRepository.findAllCategory());
    }

    // 카테고리 생성
    public ResponseEntity<Void> createCategory(CreateCategoryDto createCategoryDto) {
        try {
            // 중복 검사
            categoryRepository.findByName(createCategoryDto.getName()).ifPresent(e -> {
                throw new DuplicateKeyException("이미 존재하는 카테고리");
            });

            Category category = Category.builder()
                    .name(createCategoryDto.getName())
                    .build();

            categoryRepository.save(category);

            return ResponseEntity.ok().build();
        } catch (DuplicateKeyException e) {
            log.error("이미 존재하는 카테고리입니다.");
            return ResponseEntity.internalServerError().build();
        }

    }

    // 카테고리 수정 (보이기 / 숨기기)
    public ResponseEntity<Void> modifyCategory(ModifyCategoryDto modifyCategoryDto) {
        try {
            // 없는 카테고리면 예외
            Category category = categoryRepository.findById(modifyCategoryDto.getId()).orElseThrow(EntityNotFoundException::new);
            category.changeUserYN(modifyCategoryDto.getUseYN());

            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            log.error("존재하지 않는 카테고리입니다.");
            return ResponseEntity.internalServerError().build();
        }
    }
}
