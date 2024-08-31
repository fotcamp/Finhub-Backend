package fotcamp.finhub.admin.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.admin.domain.Manager;
import fotcamp.finhub.admin.dto.process.FcmMemberInfoProcessDto;
import fotcamp.finhub.admin.dto.request.CreateFcmMessageRequestDto;
import fotcamp.finhub.admin.dto.response.FcmMemberInfoListResponseDto;
import fotcamp.finhub.admin.dto.response.FcmResponseDto;
import fotcamp.finhub.admin.repository.ManagerRepository;
import fotcamp.finhub.admin.repository.NotificationRepository;
import fotcamp.finhub.common.api.ApiCommonResponse;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.MemberNotification;
import fotcamp.finhub.common.domain.Notification;
import fotcamp.finhub.main.repository.AgreementRepository;
import fotcamp.finhub.main.repository.MemberNotificationRepository;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final AgreementRepository agreementRepository;

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

        List<String> failList = new ArrayList<>();

        try { // 0 : 멤버 이메일 리스트 , 1 : 관리자 이메일 리스트, 2 : 푸시 허용한 멤버 전체, 3 : 푸시 설정 무관 관리자 전체, 4 : 멤버+관리자전체
            if (dto.getType() == 0) {
                checkAvailableTargetList(dto.getTarget());
                failList.addAll(sendNotificationsToMembers(dto.getTarget(), accessToken, apns, dataContent, notification, dbNotification));
            } else if (dto.getType() == 1) {
                checkAvailableTargetList(dto.getTarget());
                failList.addAll(sendNotificationsToManagers(dto.getTarget(), accessToken, apns, dataContent, notification));
            } else if (dto.getType() == 2) {
                failList.addAll(sendNotificationsToAllMembers(accessToken, apns, dataContent, notification, dbNotification));
            } else if (dto.getType() == 3) {
                failList.addAll(sendNotificationsToAllManagers(accessToken, apns, dataContent, notification));
            } else if (dto.getType() == 4) {
                failList.addAll(sendNotificationsToAllManagers(accessToken, apns, dataContent, notification));
                failList.addAll(sendNotificationsToAllMembers(accessToken, apns, dataContent, notification, dbNotification));
            } else {
                return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("TYPE을 확인하세요."));
            }
        } catch (FcmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseWrapper.fail(e.getMessage()));
        }
        notificationRepository.save(dbNotification);
        return ResponseEntity.ok(ApiResponseWrapper.success(new FcmResponseDto(failList)));
    }

    private void checkAvailableTargetList(List<String> targetList) {
        if (targetList.isEmpty()) {
            throw new FcmException("Target 필드가 비어있습니다.");
        }
    }

    private List<String> sendNotificationsToAllManagers(
            String accessToken, FcmMessageProcessDto.Apns apns, FcmMessageProcessDto.DataContent dataContent, FcmMessageProcessDto.Notification notification) throws JsonProcessingException {
        List<Manager> allManagers = managerRepository.findAll();
        List<String> failList = new ArrayList<>();
        for (Manager manager : allManagers) {
            if (manager.getFcmToken() != null) {
                FcmMessageProcessDto.FcmMessage message = buildFcmMessage(manager.getFcmToken(), apns, dataContent, notification);
                try {
                    sendFcmMessage(accessToken, message);
                } catch (FcmException e) {
                    failList.add(manager.getName());
                }
            }
        }
        return failList;
    }

    private List<String> sendNotificationsToAllMembers(
            String accessToken, FcmMessageProcessDto.Apns apns, FcmMessageProcessDto.DataContent dataContent, FcmMessageProcessDto.Notification notification, Notification newNotification) throws FcmException, JsonProcessingException {
        List<Member> activeMembers = agreementRepository.findMembersByPushYn(true);
        List<String> failList = new ArrayList<>();
        List<MemberNotification> notificationsToSave = new ArrayList<>();
        for (Member member : activeMembers) {
            if (member.getFcmToken() != null) {
                FcmMessageProcessDto.FcmMessage message = buildFcmMessage(member.getFcmToken(), apns, dataContent, notification);
                try {
                    sendFcmMessage(accessToken, message);
                } catch (FcmException e) {
                    failList.add(member.getName());
                }
            }
            MemberNotification newSendNoti = new MemberNotification(member, newNotification);
            notificationsToSave.add(newSendNoti);
        }
        memberNotificationRepository.saveAll(notificationsToSave); // save 메소드로 매번 쿼리 날리는 작업 대신 saveAll 메소드로 대체
        return failList;
    }

    private List<String> sendNotificationsToMembers(List<String> memberList, String accessToken, FcmMessageProcessDto.Apns apns, FcmMessageProcessDto.DataContent dataContent, FcmMessageProcessDto.Notification notification, Notification newNotification) throws FcmException, JsonProcessingException {
        // 멤버리스트 순회하면서 푸시허용한 멤버에게만 fcm 전송
        List<Member> activeMembers = agreementRepository.findMembersByPushYnTrueAndEmails(memberList);
        List<String> failList = new ArrayList<>();
        List<MemberNotification> notificationsToSave = new ArrayList<>();
        for (Member m : activeMembers) {
            if (m.getFcmToken() != null) {
                FcmMessageProcessDto.FcmMessage message = buildFcmMessage(m.getFcmToken(), apns, dataContent, notification);
                try {
                    sendFcmMessage(accessToken, message);
                } catch (FcmException e) {
                    failList.add(m.getName());
                }
            }
            MemberNotification newSendNoti = new MemberNotification(m, newNotification);
            notificationsToSave.add(newSendNoti);
        }
        memberNotificationRepository.saveAll(notificationsToSave);
        return failList;
    }

    private List<String> sendNotificationsToManagers(List<String> managerList, String accessToken, FcmMessageProcessDto.Apns apns, FcmMessageProcessDto.DataContent dataContent, FcmMessageProcessDto.Notification notification) throws FcmException, JsonProcessingException {
        final List<Manager> activeManagers = managerRepository.findByPushYnAndEmails(managerList);
        List<String> failList = new ArrayList<>();
        for (Manager m : activeManagers) {
            if (m.getFcmToken() != null) {
                FcmMessageProcessDto.FcmMessage message = buildFcmMessage(m.getFcmToken(), apns, dataContent, notification);
                try {
                    sendFcmMessage(accessToken, message);
                } catch (FcmException e) {
                    failList.add(m.getName());
                }
            }
        }
        return failList;
    }

    private void sendFcmMessage(String accessToken, FcmMessageProcessDto.FcmMessage message) throws JsonProcessingException, FcmException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        String jsonMessage = objectMapper.writeValueAsString(Map.of("message", message));
        HttpEntity<String> entity = new HttpEntity<>(jsonMessage, headers);

        String fcmUrl = "https://fcm.googleapis.com/v1/projects/" + fcmConfig.getProjectId() + "/messages:send";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(fcmUrl, HttpMethod.POST, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to send FCM message: {}", response.getBody());
            }
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException - 전송 실패 대상 토큰: {}", message.getToken());
        } catch (RestClientException e) {
            log.error("RestClientException - 전송 실패 대상 토큰: {}", message.getToken());
        }
    }

    private String getAccessToken() {
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

    private FcmMessageProcessDto.FcmMessage buildFcmMessage(String token, FcmMessageProcessDto.Apns apns, FcmMessageProcessDto.DataContent dataContent, FcmMessageProcessDto.Notification notification) {
        return FcmMessageProcessDto.FcmMessage.builder()
                .token(token)
                .notification(notification)
                .data(dataContent)
                .apns(apns)
                .build();
    }

    private FcmMessageProcessDto.DataContent buildDataContent(CreateFcmMessageRequestDto dto) throws JsonProcessingException {
        String actionJson = objectMapper.writeValueAsString(dto.getAction());
        return FcmMessageProcessDto.DataContent.builder()
                .title(dto.getTitle())
                .body(dto.getContent())
                .view(dto.getView())
                .action(actionJson)
                .build();
    }

    private FcmMessageProcessDto.Notification buidNotification(CreateFcmMessageRequestDto dto) {
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
                .mutableContent(1L)
                .build();

        FcmMessageProcessDto.Payload payload = FcmMessageProcessDto.Payload.builder()
                .aps(aps).build();

        return FcmMessageProcessDto.Apns.builder()
                .payload(payload)
                .build();
    }

    public ResponseEntity<ApiCommonResponse<FcmMemberInfoListResponseDto>> getFcmMemberInfo(int type, String method) {
        List<Member> memberList = null;
        switch (type){
            case 1:
                memberList = memberRepository.findByEmail(method);
                break;
            case 2:
                memberList = memberRepository.findByName(method);
                break;
            case 3:
                memberList = memberRepository.findByUuid(method);
                break;
            default:
                throw new FcmException("type 다시 확인해주세요.");
        }

        List<FcmMemberInfoProcessDto> memberInfoProcessDto =
                memberList.stream()
                        .map(member -> FcmMemberInfoProcessDto.builder()
                                .id(member.getMemberId())
                                .name(member.getName())
                                .uuid(member.getMemberUuid())
                                .provider(member.getProvider())
                                .pushYn(member.getMemberAgreement().isPushYn()).build()).collect(Collectors.toList()); // 조회 대상 개수가 적어 n+1 문제 발생시, 성능에 영향 없을 것으로 예상
        return ResponseEntity.ok(ApiCommonResponse.success(new FcmMemberInfoListResponseDto(memberInfoProcessDto)));
    }
}
