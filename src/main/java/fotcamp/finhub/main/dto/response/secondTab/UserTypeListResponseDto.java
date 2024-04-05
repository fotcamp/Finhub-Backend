package fotcamp.finhub.main.dto.response.secondTab;

import fotcamp.finhub.main.dto.process.secondTab.UserTypeListProcessDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeListResponseDto {

    private List<UserTypeListProcessDto> usertypeList;

}
