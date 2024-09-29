package fotcamp.finhub.main.controller;

import fotcamp.finhub.common.api.ApiCommonResponse;
import fotcamp.finhub.common.service.SlackWebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Tag(name = "Sentry api 처리", description = "sentry api")
@RestController
@RequestMapping("/api/v1/sentry")
@RequiredArgsConstructor
public class SentryController {

    private final SlackWebhookService slackWebhookService;

    @PostMapping("/")
    @Operation(summary = "Sentry 에러 메시지 로그 수신 api", description = "webhook url을 등록해줘야함")
    public ResponseEntity<ApiCommonResponse> receiveSentryMessage(@RequestBody String logMessage){
        System.out.println(logMessage);
        String titleRegex = "\"title\":\"([^\"]+)\"";
        String webUrlRegex = "\"web_url\":\"([^\"]+)\"";
        String userAgentRegex = "\\[\"User-Agent\",\\s*\"([^\"]+)\"\\]";

        Pattern titlePattern = Pattern.compile(titleRegex);
        Pattern webUrlPattern = Pattern.compile(webUrlRegex);
        Pattern userAgentPattern = Pattern.compile(userAgentRegex);

        Matcher titleMatcher = titlePattern.matcher(logMessage);
        Matcher webUrlMatcher = webUrlPattern.matcher(logMessage);
        Matcher userAgentMatcher = userAgentPattern.matcher(logMessage);

        String title = "Title not found";
        String webUrl = "Web URL not found";
        String userAgent = "User-Agent not found";

        if (titleMatcher.find()) {
            title = titleMatcher.group(1);
        }

        if (webUrlMatcher.find()) {
            webUrl = webUrlMatcher.group(1);
        }

        if (userAgentMatcher.find()) {
            userAgent = userAgentMatcher.group(1);
        }
        slackWebhookService.sentErrorLogMessage(title, webUrl, userAgent);
        return ResponseEntity.ok(ApiCommonResponse.success("success"));
    }


}
