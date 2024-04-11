package fotcamp.finhub.main.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import fotcamp.finhub.common.exception.FcmException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.Collections;



@Slf4j
@Getter
@Configuration
public class FcmConfig {

    @Value("${firebase.firebaseConfigPath}")
    private String firebaseConfigPath;

    @Value("${firebase.scope}")
    private String scope;

    @Value("${firebase.id}")
    private String projectId;

    /**
     * FCM 초기화
     * 어플리케이션 시작 시 한번만 실행된다.
     * @PostConstruct 의존성 주입이 이루어진 후 초기화메서드를 수행하기 위한 어노테이션.
     *                호출을 따로 해주지 않더라도 어플리케이션 시작 시 한번만 실행하도록 보장된다. 파라미터가 존재하면 안된다.
     * setCredentials()에서 키를 이용해 인증한다.
     * GoogleCredentials : OAuth2를 이용해 GoogleApi 호출을 승인하기 위한 객체
     */

    @PostConstruct
    public void firebaseInit() {
        try {
            ClassPathResource serviceAccount = new ClassPathResource(firebaseConfigPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream())
                            .createScoped(Collections.singleton(scope)))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase init start");
            }
        } catch(IOException e) {
            throw new FcmException("firebase init fail!!");
        }
    }
}
