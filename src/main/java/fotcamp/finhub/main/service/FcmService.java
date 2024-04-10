package fotcamp.finhub.main.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import org.springframework.http.*;
import com.google.auth.oauth2.GoogleCredentials;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.exception.FcmException;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.config.FcmConfig;
import fotcamp.finhub.main.dto.process.FcmMessageProcessDto;
import fotcamp.finhub.main.dto.request.FcmMessageRequestDto;
import fotcamp.finhub.main.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class FcmService {

    private final MemberRepository memberRepository;
    private final FcmConfig fcmConfig;
    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponseWrapper> newFcmToken(CustomUserDetails userDetails, String fcmToken) {
        Member member = memberRepository.findById(userDetails.getMemberIdasLong())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        member.updateFcmToken(fcmToken);
        memberRepository.save(member);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public ResponseEntity<ApiResponseWrapper> makeFcmMessage(FcmMessageRequestDto dto) throws JsonProcessingException {
        List<Member> activeMembers = memberRepository.findByPushYn(true);
        String accessToken = getAccessToken();

        FcmMessageProcessDto.DataContent dataContent = FcmMessageProcessDto.DataContent.builder()
                .title(dto.getTitle())
                .body(dto.getContent())
                .view(dto.getView())
                .build();

        FcmMessageProcessDto.Alert alert = FcmMessageProcessDto.Alert.builder()
                .title(dto.getTitle())
                .body(dto.getContent())
                .build();

        FcmMessageProcessDto.Aps aps = FcmMessageProcessDto.Aps.builder()
                .alert(alert)
                .build();

        FcmMessageProcessDto.Payload payload = FcmMessageProcessDto.Payload.builder()
                .aps(aps).build();

        FcmMessageProcessDto.Apns apns = FcmMessageProcessDto.Apns.builder()
                .payload(payload)
                .build();

        for(Member member : activeMembers){
            if(member.getFcmToken() != null){
                try{
                    FcmMessageProcessDto.FcmMessage message = FcmMessageProcessDto.FcmMessage.builder()
                            .token(member.getFcmToken())
                            .data(dataContent)
                            .apns(apns)
                            .build();

                    sendFcmMessage(accessToken, message);
                }catch (Exception e){
                    throw new FcmException("fcm message processing error");
                }
            }
        }
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    public void sendFcmMessage(String accessToken, FcmMessageProcessDto.FcmMessage message) throws JsonProcessingException {
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
