package fotcamp.finhub.main.dto.process;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FcmMessageProcessDto {

    private final FcmMessage message;

    @Builder
    @Getter
    public static class FcmMessage {
        private String token;
        private Notification notification;
        private DataContent data;
        private Apns apns;
    }
    @Builder
    @Getter
    public static class Notification{
        private String title;
        private String body;
    }
    @Builder
    @Getter
    public static class DataContent{
        private String title;
        private String body;
        private String view;
        private String action;
    }
    @Builder
    @Getter
    public static class Apns{
        private Payload payload;
    }
    @Builder
    @Getter
    public static class Payload{
        private Aps aps;
    }
    @Builder
    @Getter
    public static class Aps{
        private Alert alert;
        @JsonProperty("mutable-content")
        private Long mutableContent;
    }
    @Builder
    @Getter
    public static class Alert{
        private String title;
        private String body;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    public static class Action{
        private String date;
        private Long id;

        @JsonCreator
        public Action(@JsonProperty("date")String date,@JsonProperty("id") Long id) {
            this.date = date;
            this.id = id;
        }
    }

}
