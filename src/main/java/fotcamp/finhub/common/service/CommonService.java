package fotcamp.finhub.common.service;

import fotcamp.finhub.common.dto.process.PageInfoProcessDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    public PageInfoProcessDto setPageInfo(Page<?> entityPageList) {
        return new PageInfoProcessDto(entityPageList.getNumber()+1, entityPageList.getTotalPages(), entityPageList.getSize(), entityPageList.getTotalElements());
    }
}
