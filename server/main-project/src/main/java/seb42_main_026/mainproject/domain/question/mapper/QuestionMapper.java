package seb42_main_026.mainproject.domain.question.mapper;

import org.mapstruct.Mapper;
import seb42_main_026.mainproject.domain.answer.dto.AnswerDto;
import seb42_main_026.mainproject.domain.answer.entity.Answer;
import seb42_main_026.mainproject.domain.member.entity.Member;
import seb42_main_026.mainproject.domain.question.dto.QuestionDto;
import seb42_main_026.mainproject.domain.question.entity.Question;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    default Question questionPostDtoToQuestion(QuestionDto.Post questionPostDto) {
        Question question = new Question();
        question.setTitle(questionPostDto.getTitle());
        question.setContent(questionPostDto.getContent());

        Member member = new Member();
        member.setMemberId(questionPostDto.getMemberId());

        question.setMember(member);

        return question;
    };

    default QuestionDto.DetailResponse questionToQuestionDetailResponseDto(Question question) {
        QuestionDto.DetailResponse detailResponse = new QuestionDto.DetailResponse();
        detailResponse.setQuestionId(question.getQuestionId());
        detailResponse.setMemberId(question.getMember().getMemberId());
        detailResponse.setTitle(question.getTitle());
        detailResponse.setContent(question.getContent());
        detailResponse.setNickname(question.getMember().getNickname());
        detailResponse.setCreatedAt(question.getCreatedAt());
        detailResponse.setQuestionStatus(question.getQuestionStatus().getDescription());
        detailResponse.setLikeCount(question.getLikeCount());

        List<Answer> answers = question.getAnswers();
        List<AnswerDto.Response> answerResponses = answers.stream()
                        .map(answer -> {
                            AnswerDto.Response answerResponse = new AnswerDto.Response();
                            answerResponse.setAnswerId(answer.getAnswerId());
                            answerResponse.setMemberId(answer.getMember().getMemberId());
                            answerResponse.setNickname(answer.getMember().getNickname());
                            answerResponse.setContent(answer.getContent());
                            answerResponse.setAnswerStatus(answer.getAnswerStatus().getStatus());
                            answerResponse.setCreatedAt(answer.getCreatedAt());
////                            answerResponseDto.setProfileImgUrl(answer.getProfileImgUrl());
////                            answerResponseDto.setVoiceFileUrl(answer.getVoiceFileUrl());
////                            answerResponseDto.setLikeCount(answer.getLikeCount());
                            return answerResponse;
                        }).collect(Collectors.toList());

        detailResponse.setAnswers(answerResponses);

        return detailResponse;
    };

    List<QuestionDto.Response> questionsToQuestionResponseDtos(List<Question> questions);
}
