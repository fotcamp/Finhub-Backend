package fotcamp.finhub.main.controller;

import fotcamp.finhub.common.api.ApiCommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Sentry api 처리", description = "sentry api")
@RestController
@RequestMapping("/api/v1/sentry")
@RequiredArgsConstructor
public class SentryController {

    @PostMapping("/")
    @Operation(summary = "Sentry 에러 메시지 로그 수신 api", description = "webhook url을 등록해줘야함")
    public ResponseEntity<ApiCommonResponse> receiveSentryMessage(@RequestBody Map<String, Object> payload){
        System.out.println("Received event from Sentry: " + payload);
        return ResponseEntity.ok(ApiCommonResponse.success("success"));
    }
}
