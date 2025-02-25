package seb42_main_026.mainproject.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seb42_main_026.mainproject.domain.answer.service.AnswerService;
import seb42_main_026.mainproject.domain.comment.entity.Comment;
import seb42_main_026.mainproject.domain.comment.repository.CommentRepository;
import seb42_main_026.mainproject.domain.member.service.MemberService;
import seb42_main_026.mainproject.exception.CustomException;
import seb42_main_026.mainproject.exception.ExceptionCode;
import seb42_main_026.mainproject.utils.CustomBeanUtils;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final MemberService memberService;
    private final AnswerService answerService;
    private final CommentRepository commentRepository;
    private final CustomBeanUtils<Comment> customBeanUtils;

    public Comment createComment(Comment comment){
//        //로그인된 회원인지 확인
//        memberService.verifyLoginMember(comment.getMember().getMemberId());

        //답변 확인
        answerService.findAnswer(comment.getAnswer().getAnswerId());

        return commentRepository.save(comment);
    }

    public void updateComment(Comment comment, long memberId){
        Comment foundComment = findComment(comment.getCommentId());

//        // 로그인된 회원인지 확인, 권한 확인
//        memberService.verifyLoginMember(foundComment.getMember().getMemberId());

        memberService.verifyMemberByMemberId(foundComment.getMember().getMemberId(), memberId);

        customBeanUtils.copyNonNullProperties(comment, foundComment);

//        return commentRepository.save(foundComment);
    }

    public void deleteComment(long commentId, long memberId){
        Comment foundComment = findComment(commentId);

//        memberService.verifyLoginMember(comment.getMember().getMemberId());

        memberService.verifyMemberByMemberId(foundComment.getMember().getMemberId(), memberId);

        commentRepository.delete(foundComment);
    }

    @Transactional(readOnly = true)
    public Comment findComment(long commentId){
        return findVerifiedComment(commentId);
    }

    private Comment findVerifiedComment(long commentId){
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        return optionalComment.orElseThrow(() -> new CustomException(ExceptionCode.COMMENT_NOT_FOUND));
    }
}
