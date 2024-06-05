package fotcamp.finhub.main.service;

import fotcamp.finhub.admin.dto.process.TopicIdTitleDto;
import fotcamp.finhub.admin.repository.GptColumnRepository;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.*;
import fotcamp.finhub.common.dto.process.PageInfoProcessDto;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.common.service.AwsS3Service;
import fotcamp.finhub.common.service.CommonService;
import fotcamp.finhub.main.dto.request.BlockMemberRequestDto;
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

import java.util.Collections;
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
    private final ReportReasonsRepository reportReasonsRepository;
    private final CommentsReportRepository commentsReportRepository;
    private final BlockRepository blockRepository;

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
        if (member.getNickname() == null) {
            return ResponseEntity.ok(ApiResponseWrapper.fail("닉네임 설정 필요"));
        }

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
    public ResponseEntity<ApiResponseWrapper> getColumnComment(CustomUserDetails userDetails, Long id, Long type, Pageable pageable) {
        String loginCheck;
        Long memberId;
        if (userDetails == null) {
            loginCheck = "N";
            memberId = null;
        } else {
            loginCheck = "Y";
            memberId = userDetails.getMemberIdasLong();
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        }

        GptColumn gptColumn = gptColumnRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("GPT COLUMN이 존재하지 않습니다."));
        List<Member> blockMemberList = (memberId != null) ? blockRepository.findBlockMemberByMemberId(memberId) : Collections.emptyList();
        Page<Comments> commentsList;

        if (blockMemberList.isEmpty()) {
            if (type == 1) { // 인기순
                commentsList = commentsRepository.findByGptColumnAndUseYnOrderByCreatedTimeDesc(gptColumn, "Y", pageable);
            } else if (type == 2) { // 최신순
                commentsList = commentsRepository.findByGptColumnAndUseYnOrderByTotalLikeDescCreatedTimeDesc(gptColumn, "Y", pageable);
            } else {
                return ResponseEntity.ok(ApiResponseWrapper.fail("type을 확인해주세요", type));
            }
        } else {
            if (type == 1) { // 인기순
                commentsList = commentsRepository.findByGptColumnAndUseYnAndMemberNotInOrderByTotalLikeDescCreatedTimeDesc(gptColumn, "Y", blockMemberList, pageable);
            } else if (type == 2) { // 최신순
                commentsList = commentsRepository.findByGptColumnAndUseYnAndMemberNotInOrderByCreatedTimeDesc(gptColumn, "Y", blockMemberList, pageable);
            } else {
                return ResponseEntity.ok(ApiResponseWrapper.fail("type을 확인해주세요", type));
            }
        }

        List<CommentResponseDto> responseList = commentsList.getContent().stream()
                .map(comment -> {
                    Member commentWriter = comment.getMember();
                    String avatarPath = Optional.ofNullable(commentWriter.getUserAvatar())
                            .map(UserAvatar::getAvatar_img_path)
                            .map(awsS3Service::combineWithBaseUrl)
                            .orElse(null); // getUserAvatar()가 null이면 null 반환
                    if ("Y".equals(loginCheck)) { // 로그인 한 유저면
                        return new CommentResponseDto(commentWriter, comment, avatarPath, comment.getMember().getMemberId().equals(memberId));
                    } else { // 로그인 하지않은 유저면
                        return new CommentResponseDto(commentWriter, comment, avatarPath, false);
                    }
                }).toList();
        PageInfoProcessDto PageInfoProcessDto = commonService.setPageInfo(commentsList);
        return ResponseEntity.ok(ApiResponseWrapper.success(new ColumnResponse(responseList, PageInfoProcessDto)));
    }

    // 댓글 수정
    public ResponseEntity<ApiResponseWrapper> commentPut(CustomUserDetails userDetails, CommentRequestDto dto) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
        Long memberId = userDetails.getMemberIdasLong();
        memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        Comments comments = commentsRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("댓글 ID가 존재하지 않습니다."));
        if (!comments.getMember().getMemberId().equals(memberId)) {
            return ResponseEntity.ok(ApiResponseWrapper.fail("자신의 댓글만 수정 할 수 있습니다."));
        }
        comments.modifyContent(dto.comment());
        commentsRepository.save(comments);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    // 댓글 삭제
    public ResponseEntity<ApiResponseWrapper> commentDelete(CustomUserDetails userDetails, CommentDeleteRequestDto dto) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
        Long memberId = userDetails.getMemberIdasLong();
        memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        Comments comments = commentsRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("댓글 ID가 존재하지 않습니다."));
        if (!comments.getMember().getMemberId().equals(memberId)) {
            return ResponseEntity.ok(ApiResponseWrapper.fail("자신의 댓글만 삭제 할 수 있습니다."));
        }
        if ("N".equals(comments.getUseYn())) {
            return ResponseEntity.ok(ApiResponseWrapper.fail("이미 삭제한 댓글입니다."));
        }
        comments.modifyUseYn(); // 삭제처리 -> useYN "N"

        if (commentsReportRepository.findByReportedComment(comments).isPresent()) { // 이미 신고된 댓글 이였을 경우 싱크 맞추기 위해 N 처리
            CommentsReport commentsReport = commentsReportRepository.findByReportedComment(comments).get();
            commentsReport.modifyUseYn();
        }
        commentsRepository.save(comments);
        return ResponseEntity.ok(ApiResponseWrapper.success());

    }

    // 댓글 신고 이유 리스트 조회
    public ResponseEntity<ApiResponseWrapper> commentReasons() {
        List<ReportReasons> reportReasons = reportReasonsRepository.findAllByUseYnOrderByIdAsc("Y");
        List<ReportReasonListDto> reasonList = reportReasons.stream().map(reason -> {
            return new ReportReasonListDto(reason);
        }).toList();

        return ResponseEntity.ok(ApiResponseWrapper.success(new ReportReasonAnswerDto(reasonList)));
    }

    // 댓글 신고하기
    public ResponseEntity<ApiResponseWrapper> commentReport(CustomUserDetails userDetails, CommentReportRequestDto dto) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
        Long memberId = userDetails.getMemberIdasLong();
        Member reporterMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        Comments reportedComment = commentsRepository.findById(dto.commentId()).orElseThrow(() -> new EntityNotFoundException("댓글ID 존재하지 않습니다."));
        Member reportedMember = reportedComment.getMember();
        ReportReasons reportReason = reportReasonsRepository.findById(dto.reportId()).orElseThrow(() -> new EntityNotFoundException("신고사유ID가 존재하지 않습니다."));
        if (commentsReportRepository.findByReportedCommentAndReporterMember(reportedComment, reporterMember).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("이미 신고한 댓글입니다."));
        }
        CommentsReport commentsReport = CommentsReport.builder()
                .reportedComment(reportedComment)
                .reporterMember(reporterMember)
                .reportedMember(reportedMember)
                .reportReasons(reportReason)
                .useYn("Y")
                .build();
        commentsReportRepository.save(commentsReport);
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }

    // 사용자 차단하기
    public ResponseEntity<ApiResponseWrapper> blockMember(CustomUserDetails userDetails, BlockMemberRequestDto dto){
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(ApiResponseWrapper.fail("로그인이 필요한 기능입니다."));
        }
        Long memberId = userDetails.getMemberIdasLong();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        Member blockMember = memberRepository.findById(dto.memberId()).orElseThrow(() -> new EntityNotFoundException("회원ID가 존재하지 않습니다."));
        blockRepository.save(new Block(member, blockMember));
        blockRepository.save(new Block(blockMember, member));
        return ResponseEntity.ok(ApiResponseWrapper.success());
    }
}
