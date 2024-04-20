package fotcamp.finhub.admin.dto.response;

import fotcamp.finhub.common.domain.ReportReasons;
import lombok.Getter;

@Getter
public class ReportReasonsDto {
    private Long id;
    private String reason;
    private String useYn;

    public ReportReasonsDto(ReportReasons reportReasons) {
        this.id = reportReasons.getId();
        this.reason = reportReasons.getReason();
        this.useYn = reportReasons.getUseYn();
    }
}
