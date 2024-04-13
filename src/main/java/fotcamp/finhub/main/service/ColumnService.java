package fotcamp.finhub.main.service;

import fotcamp.finhub.admin.dto.process.TopicIdTitleDto;
import fotcamp.finhub.admin.repository.GptColumnRepository;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.GptColumn;
import fotcamp.finhub.common.dto.process.PageInfoProcessDto;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.common.service.CommonService;
import fotcamp.finhub.main.dto.response.column.ColumnListAnswerDto;
import fotcamp.finhub.main.dto.response.column.ColumnListDto;
import fotcamp.finhub.main.repository.CommentsLikeRepository;
import fotcamp.finhub.main.repository.CommentsRepository;
import fotcamp.finhub.main.repository.PostsLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ColumnService {
    private final PostsLikeRepository postsLikeRepository; // 게시글 좋아요 레포지토리
    private final CommentsRepository commentsRepository; // 댓글 레포지토리
    private final CommentsLikeRepository commentsLikeRepository; // 댓글 좋아요 레포지토리
    private final GptColumnRepository gptColumnRepository; // 컬럼 레포지토리
    private final CommonService commonService;
    private final AwsS3Service awsS3Service;


    // column 리스트 조회
    public ResponseEntity<ApiResponseWrapper> getColumnList(Pageable pageable) {
        Page<GptColumn> gptColumns = gptColumnRepository.findByUseYN("Y", pageable);
        List<ColumnListDto> columnListDto = gptColumns.getContent().stream().map(gptColumn -> {
            List<TopicIdTitleDto> topicList = gptColumn.getTopicGptColumnList().stream().map(topicGptColumn -> {
                return new TopicIdTitleDto(topicGptColumn);
            }).toList();
            return new ColumnListDto(gptColumn.getId(), gptColumn.getTitle(), gptColumn.getCreatedTime().toLocalDate(),
                    awsS3Service.combineWithBaseUrl(gptColumn.getBackgroundUrl()), topicList);
        }).toList();

        PageInfoProcessDto pageInfoProcessDto = commonService.setPageInfo(gptColumns);
        return ResponseEntity.ok(ApiResponseWrapper.success(new ColumnListAnswerDto(columnListDto, pageInfoProcessDto)));
    }
}
