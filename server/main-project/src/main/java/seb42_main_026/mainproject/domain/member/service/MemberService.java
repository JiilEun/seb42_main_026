package seb42_main_026.mainproject.domain.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import seb42_main_026.mainproject.cloud.service.S3StorageService;
import seb42_main_026.mainproject.domain.member.dto.MemberDto;
import seb42_main_026.mainproject.domain.member.entity.Member;
import seb42_main_026.mainproject.domain.member.entity.Score;
import seb42_main_026.mainproject.domain.member.repository.MemberRepository;
import seb42_main_026.mainproject.domain.member.repository.ScoreRepository;
import seb42_main_026.mainproject.exception.CustomException;
import seb42_main_026.mainproject.exception.ExceptionCode;
import seb42_main_026.mainproject.security.utils.CustomAuthorityUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;
    private final ScoreRepository scoreRepository;
    private final S3StorageService s3StorageService;

    public Member createMember(Member member, MultipartFile profileImage) {
        verifyExistsEmail(member.getEmail());
        verifyExistsNickName(member.getNickname());

        String encryptedPassword = passwordEncoder.encode(member.getPassword()); // Password 암호화

        member.setPassword(encryptedPassword);

        List<String> roles = authorityUtils.createRoles(member.getEmail()); // DB에 User Role 저장

        member.setRoles(roles);

        // 이미지 url 저장
        if (profileImage != null) {
            String encodedFileName = s3StorageService.encodeFileName(profileImage);
            member.setProfileImageUrl(s3StorageService.getFileUrl(encodedFileName));
            s3StorageService.store(profileImage, encodedFileName);
        } else {
            member.setProfileImageUrl(null);
        }

        Member savedMember = memberRepository.save(member);

        setScore(savedMember.getMemberId());

        return savedMember;
    }

    public List<Score> getRank() {
        return scoreRepository.findTop10ByOrderByScoreDescModifiedAtAscCreatedAtAsc();
    }

//    @Transactional(readOnly = true)
//    public Member getMember(Long memberId) {
//        Member member = findVerifiedMember(memberId);
//
//        return member;
//    }

    @Transactional(readOnly = true)
    public Member findMember(Long memberId) {
        // 로그인한 회원인지 검증
        verifyLoginMember(memberId);

        return findVerifiedMember(memberId);
    }

    public void updateNickname(Member member) {
        // 로그인 멤버 권한 검사
        verifyLoginMember(member.getMemberId());

        // 멤버 확인
        Member verifiedMember = findVerifiedMember(member.getMemberId());

        // 바꾸려는 닉네임 중복 확인
        verifyExistsNickName(member.getNickname());

        // 닉네임 변경
        verifiedMember.setNickname(member.getNickname());

        // score 닉네임 변경
        Score score = scoreRepository.findByMember_MemberId(member.getMemberId());
        score.setNickname(verifiedMember.getNickname());
    }

    public void updateProfileImage(long memberId, MultipartFile profileImage) {
        verifyLoginMember(memberId);

        Member verifiedMember = findVerifiedMember(memberId);
        Score score = scoreRepository.findByMember_MemberId(memberId);

        String encodedFileName = s3StorageService.encodeFileName(profileImage);
        String profileImageUrl = s3StorageService.getFileUrl(encodedFileName);

        verifiedMember.setProfileImageUrl(profileImageUrl);
        score.setProfileImageUrl(profileImageUrl);

        s3StorageService.store(profileImage, encodedFileName);
    }

//    public Member changePaaswordMember(List<Member> members){
//        // 로그인 멤버 권한 검사
//        verifyLoginMember(members.get(0).getMemberId());
//
//        // 기존 비밀번호 가져오기 위한 멤버 엔티티 가져오기
//        Member member = findVerifiedMember(members.get(0).getMemberId());
//
//        // 현재 패스워드 매치
//        // 일치 했을떄
//        if(passwordEncoder.matches(members.get(0).getPassword(),member.getPassword())){
//            System.out.println(members.get(1).getPassword());
//            // 새로운 비밀번호변경
//            String encryptedPassword = passwordEncoder.encode(members.get(1).getPassword());
//            //member.setPassword(members.get(1).getPassword());
//            member.setPassword(encryptedPassword);
//            return member;
//        // 불일치 했을때
//        }else {
//            throw new CustomException(ExceptionCode.PASSWORD_NOT_MATCH);
//        }
//    }

    public void updatePassword(Long memberId, MemberDto.PatchPassword passwordDto) {
        // 로그인 멤버 권한 검사
        verifyLoginMember(memberId);

        // 기존 비밀번호 가져오기 위한 멤버 엔티티 가져오기
        Member verifiedMember = findVerifiedMember(memberId);

        // 현재 패스워드 매치
        // 일치 했을떄
        if (passwordEncoder.matches(passwordDto.getPassword(), verifiedMember.getPassword())){
            // 변경하고 싶은 비밀번호를 암호화 한 뒤
            String encryptedPassword = passwordEncoder.encode(passwordDto.getChangePassword());
            // 새로운 비밀번호로 변경
            verifiedMember.setPassword(encryptedPassword);
            // 불일치 했을때
        } else {
            throw new CustomException(ExceptionCode.PASSWORD_NOT_MATCH);
        }
    }

    public void deleteMember(Long memberId) {
        // 로그인 멤버 권한 검사
        verifyLoginMember(memberId);

        memberRepository.deleteById(memberId);
        // 멤버 상태 변경 (원래)
        // findVerifiedMember(memberId).setMemberStatus(Member.MemberStatus.MEMBER_DELETE);
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isPresent()) {
            throw new CustomException(ExceptionCode.MEMBER_EXISTS);
        }
    }

    private void verifyExistsNickName(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);

        if (member.isPresent()) {
            throw new CustomException(ExceptionCode.NICKNAME_EXISTS);
        }
    }

    public Member findVerifiedMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);

        return member.orElseThrow(() -> new CustomException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public void verifyLoginMember(Long memberId) {
        if (!getTokenMemberId().equals(memberId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_USER);
        }
    }

    private Long getTokenMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName()).get(); //  If a value is present, returns the value, otherwise throws NoSuchElementException.

        return member.getMemberId();
    }

    public void verifyMemberByMemberId(long questionMemberId, long updateMemberId) {
        if (questionMemberId != updateMemberId) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_USER);
        }
    }

    public void setScore(Long memberId) {
        Score score = new Score();

        Member verifiedMember = findVerifiedMember(memberId);

        score.setScore(0L);
        score.setMember(verifiedMember);
        score.setNickname(verifiedMember.getNickname());

        if (verifiedMember.getProfileImageUrl() != null) {
            score.setProfileImageUrl(verifiedMember.getProfileImageUrl());
        }

        scoreRepository.save(score);
    }

//    public Score updateScore(Long memberId, Long score) {
//        Member member = findVerifiedMember(memberId);
//        Score updateScore = scoreRepository.findByMember_MemberId(memberId);
//        Long changedScore = updateScore.getScore() + score;
//
//        if (changedScore>= 50 && 100 > changedScore){
//            member.setHammerTier(Member.HammerTier.BRONZE_HAMMER);
//        } else if (changedScore >= 100 && 150 > changedScore ) {
//            member.setHammerTier(Member.HammerTier.SILVER_HAMMER);
//        } else if (changedScore >= 150 && 200 > changedScore) {
//            member.setHammerTier(Member.HammerTier.GOLD_HAMMER);
//        } else if (changedScore >= 200) {
//            member.setHammerTier(Member.HammerTier.PPONG_HAMMER);
//        }
//
//        updateScore.setScore(updateScore.getScore() + score);
//
//        return updateScore;
//    }

    public void updateScore(Long memberId, Long score) {
        Member verifiedMember = findVerifiedMember(memberId);
        Score updateScore = scoreRepository.findByMember_MemberId(memberId);
        Long changedScore = updateScore.getScore() + score;

        // 회원 망치티어 갱신
        verifiedMember.setHammerTier(updateHammerTier(changedScore));

        // 점수 갱신
        updateScore.setScore(changedScore);
    }

    private Member.HammerTier updateHammerTier(Long score) {
        int level = score >= 200 ? 4 : (int) (score / 50);

        switch (level) {
            case 1:
                return Member.HammerTier.BRONZE_HAMMER;
            case 2:
                return Member.HammerTier.SILVER_HAMMER;
            case 3:
                return Member.HammerTier.GOLD_HAMMER;
            case 4:
                return Member.HammerTier.PPONG_HAMMER;
            default:
                return Member.HammerTier.STONE_HAMMER;
        }
    }
}
