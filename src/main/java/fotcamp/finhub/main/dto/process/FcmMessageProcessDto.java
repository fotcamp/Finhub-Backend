package fotcamp.finhub.main.dto.process;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
    }
    @Builder
    @Getter
    public static class Alert{
        private String title;
        private String body;
    }

}
