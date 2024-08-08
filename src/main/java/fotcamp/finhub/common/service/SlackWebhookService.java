package fotcamp.finhub.common.service;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class SlackWebhookService {

    @Value("${slack.webhook-url}")
    private String webhookUrl;

    @Value("${slack.landing-domain}")
    private String domain;

    @Async
    public void sendMsg(String email, String text, Long id) {
        Slack slack = Slack.getInstance();
        StringBuilder sb = new StringBuilder();
        String detailUrl = String.format("%s/api/v1/admin/feedback/%d", domain, id);
        String truncatedText = text.length() > 100 ? text.substring(0, 100) + "..." : text;
        sb.append("{\n")
                .append("  \"blocks\": [\n")
                .append("    {\n")
                .append("      \"type\": \"section\",\n")
                .append("      \"text\": {\n")
                .append("        \"type\": \"mrkdwn\",\n")
                .append("        \"text\": \"*새로운 핀허브 VOC 요청사항이 발생했습니다.*\"\n")
                .append("      }\n")
                .append("    },\n")
                .append("    {\n")
                .append("      \"type\": \"section\",\n")
                .append("      \"fields\": [\n")
                .append("        {\n")
                .append("          \"type\": \"mrkdwn\",\n")
                .append("          \"text\": \"*이메일:*\\n").append(email).append("\"\n")
                .append("        },\n")
                .append("        {\n")
                .append("          \"type\": \"mrkdwn\",\n")
                .append("          \"text\": \"*요청 내용:*\\n").append(truncatedText).append("\"\n")
                .append("        }\n")
                .append("      ]\n")
                .append("    },\n")
                .append("    {\n")
                .append("      \"type\": \"section\",\n")
                .append("      \"text\": {\n")
                .append("        \"type\": \"mrkdwn\",\n")
                .append("        \"text\": \"*상세 페이지:* <").append(detailUrl).append("|여기를 클릭하세요>\"\n")
                .append("      }\n")
                .append("    }\n")
                .append("  ]\n")
                .append("}");
        String message = sb.toString();
        try {
            WebhookResponse response = slack.send(webhookUrl, message);
        } catch (IOException e) {
            log.error("slack 메시지 발송 중 문제가 발생했습니다.", e.toString());
            throw new RuntimeException(e);
        }
    }

}
