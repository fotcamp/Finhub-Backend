package fotcamp.finhub.common.dto;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


public class ErrorResponseDto {

    private int statusCode;
    private String message;
    private LocalDateTime now;

    public ErrorResponseDto(int value, String message, LocalDateTime now) {
        this.statusCode = value;
        this.message = message;
        this.now = now;
    }
}
