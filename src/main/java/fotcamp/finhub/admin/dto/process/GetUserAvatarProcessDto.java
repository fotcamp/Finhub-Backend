package fotcamp.finhub.admin.dto.process;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetUserAvatarProcessDto {
    private Long id;
    private String s3ImgUrl;
    private String createdBy;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public GetUserAvatarProcessDto(Long id, String s3ImgUrl, String createdBy, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.s3ImgUrl = s3ImgUrl;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }
}
