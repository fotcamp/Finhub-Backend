package fotcamp.finhub.admin.service.gpt;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import fotcamp.finhub.admin.repository.GptLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GptService {
    private final OpenAiService openAiService;
    private final GptLogRepository gptLogRepository;

    // GPT 질문 답변 로그 db에 저장 하고 답변 반환
    public String saveLogAndReturnAnswer(String prompt) {
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(onlyPrompt(prompt));
        CompletionChatResponseService response = CompletionChatResponseService.of(chatCompletion);

        List<String> messages = response.getMessages().stream()
                .map(CompletionChatResponseService.Message::getMessage)
                .toList();

        String answer = messages.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining());

        return answer;
    }

    private ChatCompletionRequest onlyPrompt(String prompt) {
        ChatMessage systemRole = new ChatMessage("system", "You are a teacher who teaches financial knowledge.");
        ChatMessage userRole = new ChatMessage("user", prompt);
        List<ChatMessage> messages = new ArrayList<>();

        messages.add(systemRole);
        messages.add(userRole);

        return ChatCompletionRequest.builder()
                .model("gpt-4-turbo-preview")
                .messages(messages)
                .temperature((double) 0)
                .build();
    }
}
