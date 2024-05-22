package fotcamp.finhub.admin.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.admin.domain.Manager;
import fotcamp.finhub.admin.dto.request.CreateFcmMessageRequestDto;
import fotcamp.finhub.admin.repository.ManagerRepository;
import fotcamp.finhub.admin.repository.NotificationRepository;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.MemberNotification;
import fotcamp.finhub.common.domain.Notification;
import fotcamp.finhub.main.repository.MemberNotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.*;
import com.google.auth.oauth2.GoogleCredentials;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.exception.FcmException;
import fotcamp.finhub.main.config.FcmConfig;
import fotcamp.finhub.main.dto.process.FcmMessageProcessDto;
import fotcamp.finhub.main.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FcmService {

    private final ManagerRepository managerRepository;
    private final MemberRepository memberRepository;
    private final FcmConfig fcmConfig;
    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private final MemberNotificationRepository memberNotificationRepository;

    public ResponseEntity<ApiResponseWrapper> sendFcmNotifications(CreateFcmMessageRequestDto dto) throws JsonProcessingException {
        String accessToken = getAccessToken(); // 서버 유효한지 검증
        FcmMessageProcessDto.Apns apns = buildApnsPayload(dto);
        FcmMessageProcessDto.DataContent dataContent = buildDataContent(dto);
        FcmMessageProcessDto.Notification notification = buidNotification(dto);

        Notification dbNotification = Notification.builder()
                .title(dto.getTitle())
                .message(dto.getContent())
                .url(dto.getView())
                .build();

        try {
            if ("admin".equals(dto.getTarget())) {
                sendNotificationsToManagers(accessToken, apns, dataContent, notification);
            } else if ("all".equals(dto.getTarget())) {
                sendNotificationsToManagers(accessToken, apns, dataContent, notification);
                sendNotificationsToMembers(accessToken, apns, dataContent,notification, dbNotification);
            }
            else {
                String email = dto.getTarget(); // 관리자 한명에게만 보내는 조건
                Manager manager = managerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("관리자 이메일이 존재하지 않습니다."));
                if (manager.getFcmToken() == null){
                    return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("토큰정보가 없습니다."));
                }

                FcmMessageProcessDto.FcmMessage message = buildFcmMessage(manager.getFcmToken(), apns, dataContent, notification);
                sendFcmMessage(accessToken, message);
            }
        } catch (FcmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseWrapper.fail(e.getMessage()));
        }
        notificationRepository.save(dbNotification);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    private void sendNotificationsToManagers(
            String accessToken, FcmMessageProcessDto.Apns apns, FcmMessageProcessDto.DataContent dataContent, FcmMessageProcessDto.Notification notification) throws JsonProcessingException {
        List<Manager> allManagers = managerRepository.findAll();
        for (Manager manager : allManagers) {
            if (manager.getFcmToken() != null) {
                FcmMessageProcessDto.FcmMessage message = buildFcmMessage(manager.getFcmToken(), apns, dataContent, notification);
                sendFcmMessage(accessToken, message);
            }
        }
    }

    private void sendNotificationsToMembers(
            String accessToken, FcmMessageProcessDto.Apns apns, FcmMessageProcessDto.DataContent dataContent, FcmMessageProcessDto.Notification notification,Notification newNotification) throws FcmException, JsonProcessingException {
        List<Member> activeMembers = memberRepository.findByPushYn(true);
        for (Member member : activeMembers) {
            if (member.getFcmToken() != null) {
                FcmMessageProcessDto.FcmMessage message = buildFcmMessage(member.getFcmToken(), apns, dataContent, notification );
                sendFcmMessage(accessToken, message);
            }

            MemberNotification newSendNoti = new MemberNotification(member, newNotification);
            memberNotificationRepository.save(newSendNoti);
        }
    }


    private FcmMessageProcessDto.FcmMessage buildFcmMessage(String token, FcmMessageProcessDto.Apns apns, FcmMessageProcessDto.DataContent dataContent, FcmMessageProcessDto.Notification notification) {
        return FcmMessageProcessDto.FcmMessage.builder()
                .token(token)
                .notification(notification)
                .data(dataContent)
                .apns(apns)
                .build();
    }

    private FcmMessageProcessDto.DataContent buildDataContent(CreateFcmMessageRequestDto dto) {
        return FcmMessageProcessDto.DataContent.builder()
                .title(dto.getTitle())
                .body(dto.getContent())
                .view(dto.getView())
                .build();
    }

    private FcmMessageProcessDto.Notification buidNotification(CreateFcmMessageRequestDto dto){
        return FcmMessageProcessDto.Notification.builder()
                .title(dto.getTitle())
                .body(dto.getContent())
                .build();
    }

    private FcmMessageProcessDto.Apns buildApnsPayload(CreateFcmMessageRequestDto dto) {
        FcmMessageProcessDto.Alert alert = FcmMessageProcessDto.Alert.builder()
                .title(dto.getTitle())
                .body(dto.getContent())
                .build();

        FcmMessageProcessDto.Aps aps = FcmMessageProcessDto.Aps.builder()
                .alert(alert)
                .build();

        FcmMessageProcessDto.Payload payload = FcmMessageProcessDto.Payload.builder()
                .aps(aps).build();

        return FcmMessageProcessDto.Apns.builder()
                .payload(payload)
                .build();
    }

    public void sendFcmMessage(String accessToken, FcmMessageProcessDto.FcmMessage message) throws JsonProcessingException, FcmException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        String jsonMessage = objectMapper.writeValueAsString(Map.of("message", message));
        HttpEntity<String> entity = new HttpEntity<>(jsonMessage, headers);

        String fcmUrl = "https://fcm.googleapis.com/v1/projects/"+fcmConfig.getProjectId()+"/messages:send";
        RestTemplate restTemplate = new RestTemplate();


        ResponseEntity<String> response = restTemplate.exchange(fcmUrl, HttpMethod.POST, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to send FCM message: {}", response.getBody());
            throw new IllegalStateException("FCM 메시지 전송 실패");
        }

        log.info("Successfully sent FCM message: {}", response.getBody());
    }

    public String getAccessToken(){
        try {
            final GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(fcmConfig.getFirebaseConfigPath()).getInputStream())
                    .createScoped(List.of(fcmConfig.getScope()));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            log.error("구글 토큰 요청 에러", e);
            throw new FcmException("GOOGLE_REQUEST_TOKEN_ERROR");
        }
    }

}
