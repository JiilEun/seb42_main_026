package seb42_main_026.mainproject.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class ScoreDto {

    @Getter
    @AllArgsConstructor
    public static class Response{

        private Long score;

        private String nickname;

        private LocalDateTime modifiedAt;

        private LocalDateTime createdAt;

        private String profileImageUrl;

    }
}
