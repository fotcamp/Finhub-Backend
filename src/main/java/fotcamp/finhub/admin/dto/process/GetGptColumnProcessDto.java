package fotcamp.finhub.admin.dto.process;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetGptColumnProcessDto {
    private Long id;
    private String createdBy;
    private String title;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private String useYN;

    public GetGptColumnProcessDto(Long id, String createdBy, LocalDateTime createdTime, LocalDateTime modifiedTime, String title, String useYN) {
        this.id = id;
        this.title = title;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.useYN = useYN;
    }
}
