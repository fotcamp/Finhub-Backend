package fotcamp.finhub.admin.dto.process;

import lombok.Builder;
import lombok.Getter;

import java.awt.font.TextHitInfo;

@Getter
public class FcmMemberInfoProcessDto {

    private Long id;
    private String name;
    private String uuid;
    private String provider;
    private Boolean pushYn;

    @Builder
    public FcmMemberInfoProcessDto(Long id, String name, String uuid, String provider, Boolean pushYn) {
        this.id = id;
        this.name = name;
        this.uuid = uuid;
        this.provider = provider;
        this.pushYn = pushYn;
    }
}
