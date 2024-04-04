package fotcamp.finhub.main.dto.response.secondTab;

import fotcamp.finhub.main.dto.process.secondTab.UserTypeProcessDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserTypeListResponseDto {

    private List<UserTypeProcessDto> usertypeList;

    public UserTypeListResponseDto(List<UserTypeProcessDto> usertypeList) {
        this.usertypeList = usertypeList;
    }
}
