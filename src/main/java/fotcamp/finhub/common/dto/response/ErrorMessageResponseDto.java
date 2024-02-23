package fotcamp.finhub.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ErrorMessageResponseDto {

    private int StatusCode;
    private String StatusMessage;
    private String message;


}
