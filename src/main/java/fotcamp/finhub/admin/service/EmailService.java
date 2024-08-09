package fotcamp.finhub.admin.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendReplyEmail(String to, String request, String reply) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String subject = "[Finhub] 문의에 대한 답변입니다.";
        String content = "고객님의 문의 : " + request + "답변 : " + reply;
        helper.setTo(to);
        helper.setSubject(subject); // 제목
        helper.setText(content, true); // HTML 형식의 메일을 위해 true로 설정
        mailSender.send(message);
    }
}
