package fotcamp.finhub.admin.service;

import fotcamp.finhub.admin.domain.Manager;
import fotcamp.finhub.admin.dto.LoginDto;
import fotcamp.finhub.admin.repository.AdminRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<Void> login(LoginDto loginDto) {
        try {
            Manager manager = adminRepository.findByUserId(loginDto.getId()).orElseThrow(EntityNotFoundException::new);
            if (manager.getPassword().equals(loginDto.getPassword())) {
                return ResponseEntity.ok().build(); // 200
            }
            // 비밀번호 틀렸을 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        } catch (EntityNotFoundException e) { // 유저 아이디가 틀린 경우
            return ResponseEntity.notFound().build(); // 404
        }
    }
}
