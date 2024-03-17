package fotcamp.finhub.common.domain;

import fotcamp.finhub.admin.dto.request.ModifyQuizRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Quiz extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;
    private String answer;
    private String comment;
    private LocalDate targetDate;
    private String createdBy;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.PERSIST)
    private final List<TopicQuiz> topicList = new ArrayList<>();

    public void addTopicQuizList(TopicQuiz topicQuiz) {
        topicList.add(topicQuiz);
    }

    public void modifyQuiz(ModifyQuizRequestDto quiz, String role) {
        this.question = quiz.getQuestion();
        this.answer = quiz.getAnswer();
        this.comment = quiz.getComment();
        this.createdBy = role;
    }
}
