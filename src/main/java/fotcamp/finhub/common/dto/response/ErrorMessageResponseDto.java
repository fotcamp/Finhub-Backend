package fotcamp.finhub.common.dto.response;

import fotcamp.finhub.common.api.ApiStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ErrorMessageResponseDto {

    private ApiStatus status;
    private String errorMsg;
    private Object data;
}
