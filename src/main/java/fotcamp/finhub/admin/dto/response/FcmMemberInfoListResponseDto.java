package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.FcmMemberInfoProcessDto;

import java.util.List;

public record FcmMemberInfoListResponseDto(
        List<FcmMemberInfoProcessDto> info
) { }
