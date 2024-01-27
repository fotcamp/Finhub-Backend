package fotcamp.finhub.admin.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AllUserTypeResponseDto {
    private final List<UserTypeResponseDto> usertypeList;

    public AllUserTypeResponseDto(List<UserTypeResponseDto> usertypeList) {
        this.usertypeList = usertypeList;
    }
}
