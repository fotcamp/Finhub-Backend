package fotcamp.finhub.admin.repository;

import fotcamp.finhub.admin.domain.Manager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class AdminLoginTest {

    @Autowired
    private ManagerRepository managerRepository;

    @BeforeEach
    public void beforeEach() {
        Manager adminUser = Manager.builder()
                .email("admin@naver.com")
                .password("20000")
                .build();

        managerRepository.save(adminUser);
    }

    @AfterEach
    public void afterEach() {
        managerRepository.deleteAll();
    }

    @Test
    @DisplayName("admin 관리자로그인 repository 단위테스트")
    public void adminLoginRepositoryTest() {

        // given
        Manager adminUser = Manager.builder()
                .email("admin@naver.com")
                .password("0000")
                .build();

        // when
        Manager manager = managerRepository.findByEmail("admin@naver.com").get();

        // then
        assertThat(adminUser.getEmail()).isEqualTo(manager.getEmail());
        assertThat(adminUser.getPassword()).isEqualTo(manager.getPassword());
    }

}