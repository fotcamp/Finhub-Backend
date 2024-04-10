package fotcamp.finhub.main.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class FcmMessageRequestDto {

    private String title;
    private String content;
    private String view;


    public FcmMessageRequestDto(String title, String content, String view) {
        this.title = title;
        this.content = content;
        this.view = view;
    }
}
