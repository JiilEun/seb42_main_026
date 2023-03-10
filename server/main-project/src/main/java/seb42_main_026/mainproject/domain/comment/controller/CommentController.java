package seb42_main_026.mainproject.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import seb42_main_026.mainproject.domain.comment.dto.CommentDto;
import seb42_main_026.mainproject.domain.comment.entity.Comment;
import seb42_main_026.mainproject.domain.comment.mapper.CommentMapper;
import seb42_main_026.mainproject.domain.comment.service.CommentService;
import seb42_main_026.mainproject.dto.SingleResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping //todo endpoint 작성
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper mapper;

    @PostMapping("/questions/{question-id}/{answer-id}")
    public ResponseEntity postComment(@PathVariable("question-id") @Positive long questionId,
                                      @PathVariable("answer-id") @Positive long answerId,
                                      @Valid @RequestBody CommentDto.Post commentPostDto){
        commentPostDto.setAnswerId(answerId);
        Comment comment = commentService.createComment(
                mapper.commentPostDtoToComment(commentPostDto),answerId,commentPostDto.getMemberId());

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.commentToCommentResponse(comment)), HttpStatus.CREATED);
    }

}
