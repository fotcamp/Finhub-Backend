package fotcamp.finhub.main.dto.response;


import fotcamp.finhub.main.dto.process.MyCommentsListProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyCommentsListResponseDto {

    private List<MyCommentsListProcessDto> mycomment;

    public MyCommentsListResponseDto(List<MyCommentsListProcessDto> mycomment) {
        this.mycomment = mycomment;
    }
}
