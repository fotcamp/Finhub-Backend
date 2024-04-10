package fotcamp.finhub.main.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.request.FcmMessageRequestDto;
import fotcamp.finhub.main.dto.request.FcmTokenRequestDto;
import fotcamp.finhub.main.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fcmToken")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/")
    public ResponseEntity<ApiResponseWrapper> newFcmToken(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody FcmTokenRequestDto dto){
        return fcmService.newFcmToken(userDetails, dto.getToken());
    }

    @PostMapping("/notification")
    public ResponseEntity<ApiResponseWrapper> sendFcmMessage(
            @RequestBody FcmMessageRequestDto dto) throws JsonProcessingException {
        return fcmService.makeFcmMessage(dto);
    }

}
