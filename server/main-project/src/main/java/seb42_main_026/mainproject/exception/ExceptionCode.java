package seb42_main_026.mainproject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ExceptionCode {


    MEMBER_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보가 없습니다."),

    NICKNAME_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 별명입니다."),

    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 답변입니다."),

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),

    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 질문입니다."),

    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "권한이 없는 회원입니다."),

    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "기존 비밀번호와 일치하지 않습니다."),

    ALREADY_COMPLETED_QUESTION(HttpStatus.CONFLICT, "이미 갱생된 질문입니다"),

    ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요를 했습니다"),

    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 좋아요입니다."),

    CANNOT_SELECT_OWN_ANSWER(HttpStatus.BAD_REQUEST, "본인 글을 채택 할 수 없습니다."),

    EXCEED_MAX_FILE_SIZE(HttpStatus.BAD_REQUEST, "첨부 파일의 용량이 초과되었습니다."),

    SELECT_ONLY_ONCE(HttpStatus.BAD_REQUEST, "질문 당 채택은 한개씩 가능합니다.");




    @Getter
    private HttpStatus httpStatus;

    @Getter
    private String message;


    ExceptionCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
