package fotcamp.finhub.main.repository;

import fotcamp.finhub.common.domain.CommentsReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentsReportRepositoryCustom {

    // 페이징 처리를 위한 메서드 추가
    Page<CommentsReport> searchAllTCommentsReportFilterList(Pageable pageable, String useYn, String isProcessed);

}
