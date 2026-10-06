package com.springfw.springSecurity.member.service;

import com.springfw.springSecurity.member.MemberRepository;
import com.springfw.springSecurity.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 로그인 아이디로 tb_mem을 조회해 Spring Security의 UserDetails로 변환한다.
 * 비밀번호 비교는 하지 않는다 (PasswordEncoder가 담당).
 * JWT 방식에서는 로그인 API(/api/auth/login)에서만 호출되고, 이후 요청은 토큰으로 인증된다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {   // 메서드·파라미터 이름은 프레임워크 규격
        Member member = memberRepository.findByMemId(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + username));

        return User.withUsername(member.getMemId())
            .password(member.getPwd())
            .roles(member.getRoleName())   // "1" → ROLE_ADMIN, "0" → ROLE_USER
            .build();
    }
}
