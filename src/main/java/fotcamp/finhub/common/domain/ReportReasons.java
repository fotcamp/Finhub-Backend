package fotcamp.finhub.common.domain;

import fotcamp.finhub.admin.dto.request.ReportReasonModifyRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReportReasons extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String reason;

    @Builder.Default
    private String useYn = "N";

    public void modifyReportReasons(ReportReasonModifyRequestDto dto) {
        this.reason = dto.reason();
        this.useYn = dto.useYN();
    }
}
