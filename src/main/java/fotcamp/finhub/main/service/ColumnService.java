package fotcamp.finhub.main.service;

import fotcamp.finhub.admin.dto.process.TopicIdTitleDto;
import fotcamp.finhub.admin.repository.GptColumnRepository;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.*;
import fotcamp.finhub.common.dto.process.PageInfoProcessDto;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.common.service.CommonService;
import fotcamp.finhub.main.dto.request.ScrapRequestDto;
import fotcamp.finhub.main.dto.response.column.ColumnDetailAnswerDto;
import fotcamp.finhub.main.dto.response.column.ColumnListAnswerDto;
import fotcamp.finhub.main.dto.response.column.ColumnListDto;
import fotcamp.finhub.main.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ColumnService {
    private final PostsLikeRepository postsLikeRepository; // 게시글 좋아요 레포지토리
    private final CommentsRepository commentsRepository; // 댓글 레포지토리
    private final CommentsLikeRepository commentsLikeRepository; // 댓글 좋아요 레포지토리
    private final GptColumnRepository gptColumnRepository; // 컬럼 레포지토리
    private final PostsScrapRepository postsScrapRepository;
    private final MemberRepository memberRepository;
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

    // 컬럼 상세 조회
    public ResponseEntity<ApiResponseWrapper> getColumnDetail(CustomUserDetails userDetails, Long id) {
        GptColumn gptColumn = gptColumnRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("GPT COLUMN이 존재하지 않습니다."));
        boolean isScrapped = false;
        boolean isLiked = false;

        if (userDetails != null){
            Long memberId = userDetails.getMemberIdasLong();
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
            // 유저가 스크랩했는지 확인
            isScrapped = postsScrapRepository.findFirstByGptColumnAndMember(gptColumn, member).isPresent();
            isLiked = postsLikeRepository.findFirstByGptColumnAndMember(gptColumn, member).isPresent();
        }

        List<TopicIdTitleDto> topicsDto = gptColumn.getTopicGptColumnList().stream().map(topicGptColumn -> {
            return new TopicIdTitleDto(topicGptColumn);
        }).toList();

        ColumnDetailAnswerDto answer = ColumnDetailAnswerDto.builder()
                .gptColumn(gptColumn)
                .url(awsS3Service.combineWithBaseUrl(gptColumn.getBackgroundUrl()))
                .isScrapped(isScrapped)
                .isLiked(isLiked)
                .topicList(topicsDto)
                .totalLke(postsLikeRepository.countByGptColumn(gptColumn))
                .build();

        return ResponseEntity.ok(ApiResponseWrapper.success(answer));
    }

    // 좋아요 기능
    public ResponseEntity<ApiResponseWrapper> like(CustomUserDetails userDetails, ScrapRequestDto dto) {
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        if (dto.getType() == 1) { // 컬럼 좋아요
            GptColumn gptColumn = gptColumnRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("GPT COLUMN이 존재하지 않습니다."));
            Optional<PostsLike> firstByGptColumnAndMember = postsLikeRepository.findFirstByGptColumnAndMember(gptColumn, member);
            firstByGptColumnAndMember.ifPresentOrElse(
                    postsLikeRepository::delete,
                    () -> postsLikeRepository.save(new PostsLike(gptColumn, member))
            );
        } else if (dto.getType() == 2) { // 댓글 좋아요
            Comments comments = commentsRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("댓글 ID가 존재하지 않습니다."));
            Optional<CommentsLike> firstByCommentAndMember = commentsLikeRepository.findFirstByCommentAndMember(comments, member);
            firstByCommentAndMember.ifPresentOrElse(
                    commentsLikeRepository::delete,
                    () -> commentsLikeRepository.save(new CommentsLike(comments, member))
            );
        } else {
            return ResponseEntity.ok(ApiResponseWrapper.fail("type을 확인해주세요", dto.getType()));
        }

        return ResponseEntity.ok(ApiResponseWrapper.success());
    }
}
