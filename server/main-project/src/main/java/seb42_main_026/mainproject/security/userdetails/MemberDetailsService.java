package seb42_main_026.mainproject.security.userdetails;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import seb42_main_026.mainproject.domain.member.entity.Member;
import seb42_main_026.mainproject.domain.member.repository.MemberRepository;
import seb42_main_026.mainproject.exception.CustomException;
import seb42_main_026.mainproject.exception.ExceptionCode;
import seb42_main_026.mainproject.security.utils.CustomAuthorityUtils;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new CustomException(ExceptionCode.MEMBER_NOT_FOUND));

        return new MemberDetails(findMember);

    }
    private final class MemberDetails extends Member implements UserDetails {
        MemberDetails(Member member){
            setMemberId(member.getMemberId());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            setRoles(member.getRoles());
            setNickname(member.getNickname());

        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities(){ //  User의 권한 정보를 생성한다.
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername(){
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired(){
            return true;
        }

        @Override
        public boolean isAccountNonLocked(){
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired(){
            return true;
        }

        @Override
        public boolean isEnabled(){
            return true;
        }
    }

}

