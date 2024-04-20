package fotcamp.finhub.main.dto.response.column;

import fotcamp.finhub.common.domain.ReportReasons;
import lombok.Getter;

@Getter
public class ReportReasonListDto {
    private Long id;
    private String reason;

    public ReportReasonListDto(ReportReasons reportReasons) {
        this.id = reportReasons.getId();
        this.reason = reportReasons.getReason();
    }
}
