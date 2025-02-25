package seb42_main_026.mainproject.domain.answer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seb42_main_026.mainproject.cloud.service.S3StorageService;
import seb42_main_026.mainproject.domain.answer.dto.AnswerDto;
import seb42_main_026.mainproject.domain.answer.entity.Answer;
import seb42_main_026.mainproject.domain.answer.mapper.AnswerMapper;
import seb42_main_026.mainproject.domain.answer.service.AnswerService;
import seb42_main_026.mainproject.domain.member.entity.Member;
import seb42_main_026.mainproject.dto.SingleResponseDto;
import seb42_main_026.mainproject.security.utils.UriCreator;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Validated
public class AnswerController {

    // todo DI
    private final AnswerService answerService;
    private final AnswerMapper mapper;

    /** voice file 이름만 DB에 저장해서 물리적 리소스는 S3에서 업로드 및 다운로드
     *  S3 접근 Configuration 작성 - done (test 후, 환경변수로 숨길 것.)
     *  AnswerController 수정 (1) @MultipartConfig 설정 하기 (필요한가? 고민해보기) - todo
     *  AnswerController 수정 (2) RequestPart 로 multipartFile 받기 - done
     *  Answer 엔티티에 fileName 추가 - done
     *  파일 업로드 관련 service 로직 작성 (1) S3StorageService - done
     *  파일 업로드 관련 service 로직 작성 (2) AnswerService - done
     */

    @PostMapping(value = "/{question-id}/answers", consumes =
            {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> postAnswer(@PathVariable("question-id") @Positive long questionId,
                                     @AuthenticationPrincipal Member auth,
                                     @Valid @RequestPart AnswerDto.Post answerPostDto,
                                     @RequestPart(required = false) MultipartFile voiceFile){
        answerPostDto.addQuestionId(questionId);
        answerPostDto.setMemberId(auth.getMemberId());

        Answer createdAnswer = answerService.createAnswer(
                mapper.answerPostDtoToAnswer(answerPostDto),voiceFile);

        URI location = UriCreator.createUri("/answers", createdAnswer.getAnswerId());

        return ResponseEntity.created(location).build();
    }

    /**
     * Patch 매서드로 수정기능 - done
     *  ㄴ Service 단에서 questionId와 answerId 모두 사용해야함?
     * Patch 매서드로 채택기능 - done
     */

    @PatchMapping("/{question-id}/answers/{answer-id}")
    public ResponseEntity<?> patchAnswer(@PathVariable("answer-id") @Positive long answerId,
                                         @AuthenticationPrincipal Member auth,
                                         @Valid @RequestBody AnswerDto.Patch answerPatchDto){
        answerPatchDto.setAnswerId(answerId);
        answerPatchDto.setMemberId(auth.getMemberId());

        Answer answer = mapper.answerPatchDtoToAnswer(answerPatchDto);

        answerService.updateAnswer(answer);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{question-id}/answers/{answer-id}/voiceFile",
            consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> patchVoiceFile(@PathVariable("answer-id") @Positive long answerId,
                                            @AuthenticationPrincipal Member auth,
                                            @RequestPart MultipartFile voiceFile){
        answerService.updateVoiceFile(answerId, auth.getMemberId(), voiceFile);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 채택 버튼 누르면 클라이언트에서 requestParam 으로 memberId 가져오기 [프론트와 협의 필요]- todo
    @PatchMapping("/{question-id}/answers/{answer-id}/select")
    public ResponseEntity<?> selectAnswer(@AuthenticationPrincipal Member auth,
                                          @PathVariable("question-id") @Positive long questionId,
                                          @PathVariable("answer-id") @Positive long answerId) {
        // answerService 에서 Status 바꿔주는 메서드
        answerService.selectAnswer(auth.getMemberId(), questionId, answerId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 삭제 버튼 누르면 클라이언트에서 requestParam 으로 memberId 가져오기 [프론트와 협의 필요]- todo
    @DeleteMapping("/{question-id}/answers/{answer-id}")
    public ResponseEntity<?> deleteAnswer(@AuthenticationPrincipal Member auth,
                                          @PathVariable("answer-id") @Positive long answerId){
        answerService.deleteAnswer(answerId, auth.getMemberId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
