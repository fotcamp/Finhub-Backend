package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.admin.dto.process.BannerProcessDto;
import fotcamp.finhub.admin.dto.process.UserTypeProcessDto;
import fotcamp.finhub.common.dto.process.PageInfoProcessDto;

import java.util.List;

public record AllBannerResponseDto(List<BannerProcessDto> bannerList, PageInfoProcessDto pageInfo) {
}
