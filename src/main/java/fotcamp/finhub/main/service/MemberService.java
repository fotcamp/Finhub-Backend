package fotcamp.finhub.main.service;

import fotcamp.finhub.admin.repository.TopicRepository;
import fotcamp.finhub.common.api.ApiResponseWrapper;
import fotcamp.finhub.common.domain.Member;
import fotcamp.finhub.common.domain.Topic;
import fotcamp.finhub.common.security.CustomUserDetails;
import fotcamp.finhub.main.dto.request.ChangeNicknameRequestDto;
import fotcamp.finhub.main.dto.process.SearchResultListDto;
import fotcamp.finhub.main.dto.response.SearchResponseDto;
import fotcamp.finhub.main.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;

//    public ResponseEntity<ApiResponseWrapper> home(CustomUserDetails userDetails){
//
//        //
//        // 토픽은 7개만
//        // 1. 로그인인지 비로그인인지 판단해서 로그인유저면 스크랩정보까지 줘야함
//        if (userDetails.getAuthorities() == null){
//
//        }
//    }
    
    public ResponseEntity<ApiResponseWrapper> changeNickname(CustomUserDetails userDetails , ChangeNicknameRequestDto dto){

        try{
            String newNickname = dto.getNewNickname();
            if (newNickname.length()>= 2 && newNickname.length() <= 10){
                Long memberId = userDetails.getMemberIdasLong();
                Member existingMember = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
                existingMember.updateNickname(newNickname);
                memberRepository.save(existingMember);
                return ResponseEntity.ok(ApiResponseWrapper.success("변경 완료"));
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.fail("변경조건에 맞게 작성하세요."));
            }
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.fail("해당 요청 데이터가 존재하지 않습니다."));
        }
    }

    public ResponseEntity<ApiResponseWrapper> membershipResign(CustomUserDetails userDetails){

        try{
            Long memberId = userDetails.getMemberIdasLong();
            Member existingMember = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
            memberRepository.delete(existingMember);
            return ResponseEntity.ok(ApiResponseWrapper.success("탈퇴 완료"));
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.fail("해당 요청 데이터가 존재하지 않습니다."));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseWrapper> search(String method, String keyword, int page){
        int pageSize = 4;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Topic> pageResult = null;
        switch (method) {
            case "title" -> pageResult = topicRepository.findByTitleContaining(keyword, pageable);
            case "summary" -> pageResult = topicRepository.findBySummaryContaining(keyword, pageable);
            case "both" -> pageResult = topicRepository.findByTitleContainingOrSummaryContaining(keyword,keyword, pageable);
            default -> throw new IllegalArgumentException("검색방법이 잘못되었습니다.");
        }

        List<Topic> resultList = pageResult.getContent();
        List<SearchResultListDto> response = resultList.stream()
                .map(topic -> new SearchResultListDto(topic.getTitle(), topic.getSummary())).collect(Collectors.toList());
        SearchResponseDto responseDto = new SearchResponseDto(response, page, pageResult.getTotalPages(), pageResult.getTotalElements());
        return ResponseEntity.ok(ApiResponseWrapper.success(responseDto));
    }
}
