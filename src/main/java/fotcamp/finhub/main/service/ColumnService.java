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
import fotcamp.finhub.main.dto.response.column.*;
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
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
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
            if (memberId == comments.getMember().getMemberId()) {
                return ResponseEntity.ok(ApiResponseWrapper.fail("자신의 댓글은 좋아요를 누를 수 없습니다"));
            }
            Optional<CommentsLike> firstByCommentAndMember = commentsLikeRepository.findFirstByCommentAndMember(comments, member);
            firstByCommentAndMember.ifPresentOrElse(
                    existingLike -> {
                        comments.downLike(); // 좋아요 취소
                        commentsRepository.save(comments); // 댓글 업데이트
                        commentsLikeRepository.delete(existingLike); // 기존 좋아요 삭제
                    },
                    () -> {
                        comments.upLike(); // 좋아요 추가
                        commentsRepository.save(comments); // 댓글 업데이트
                        commentsLikeRepository.save(new CommentsLike(comments, member)); // 새 좋아요 저장
                    }
            );
        } else {
            return ResponseEntity.ok(ApiResponseWrapper.fail("type을 확인해주세요", dto.getType()));
        }

        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    // 댓글 등록
    public ResponseEntity<ApiResponseWrapper> comment(CustomUserDetails userDetails, CommentRequestDto dto) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
        GptColumn gptColumn = gptColumnRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("GPT COLUMN이 존재하지 않습니다."));
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        if (commentsRepository.countByGptColumnAndMember(gptColumn, member) > 4) {
            return ResponseEntity.ok(ApiResponseWrapper.fail("한 게시글에 댓글은 최대 5개입니다."));
        }
        Comments comment = Comments.builder()
                .gptColumn(gptColumn)
                .member(member)
                .content(dto.comment())
                .build();
        commentsRepository.save(comment);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    // 컬럼 댓글 조회
    public ResponseEntity<ApiResponseWrapper> getColumnComment(CustomUserDetails userDetails, Long id, Long type) {
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        GptColumn gptColumn = gptColumnRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("GPT COLUMN이 존재하지 않습니다."));
        List<Comments> commentsList;
        if (type == 1) { // 인기순
            commentsList = commentsRepository.findByGptColumnAndUseYnOrderByTotalLikeDescCreatedTimeDesc(gptColumn, "Y");
        } else if (type == 2) { // 최신순
            commentsList = commentsRepository.findByGptColumnAndUseYnOrderByCreatedTimeDesc(gptColumn, "Y");
        } else {
            return ResponseEntity.ok(ApiResponseWrapper.fail("type을 확인해주세요", type));
        }
        List<CommentResponseDto> responseList = commentsList.stream()
                .map(comment -> {
                    String avatarPath = Optional.ofNullable(member.getUserAvatar())
                            .map(UserAvatar::getAvatar_img_path)
                            .map(awsS3Service::combineWithBaseUrl)
                            .orElse(null); // getUserAvatar()가 null이면 null 반환

                    return new CommentResponseDto(member, comment, avatarPath, comment.getMember().getMemberId().equals(memberId));
                }).toList();

        return ResponseEntity.ok(ApiResponseWrapper.success(new ColumnResponse(responseList)));
    }

    // 댓글 수정
    public ResponseEntity<ApiResponseWrapper> commentPut(CustomUserDetails userDetails, CommentRequestDto dto) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        Comments comments = commentsRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("댓글 ID가 존재하지 않습니다."));
        if (!comments.getMember().getMemberId().equals(memberId)) {
            return ResponseEntity.ok(ApiResponseWrapper.fail("자신의 댓글만 수정 할 수 있습니다."));
        }
        comments.modifyContent(dto.comment());
        commentsRepository.save(comments);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }
}
