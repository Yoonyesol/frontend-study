package com.springfw.springSecurity.oauth;

import com.springfw.springSecurity.member.MemberRepository;
import com.springfw.springSecurity.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Google 로그인 성공 직후 호출된다. (Spring Security가 인가 코드 → 토큰 교환까지 마친 뒤)
 * 역할: Google 사용자 정보로 tb_mem에 회원을 찾거나 처음이면 자동 가입시킨다.
 *
 * 모듈 2의 CustomUserDetailsService와 짝을 이루는 클래스:
 * - 아이디/비밀번호 로그인 → CustomUserDetailsService가 회원 조회
 * - Google 로그인       → CustomOidcUserService가 회원 조회·생성
 */
@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);   // ID Token 검증 + 사용자 정보 조회

        String sub = oidcUser.getSubject();                // Google 계정 고유 번호 (변하지 않음)
        String name = oidcUser.getFullName() != null ? oidcUser.getFullName() : oidcUser.getEmail();
        String picture = oidcUser.getPicture();

        memberRepository.findByMemId(Member.GOOGLE_PREFIX + sub)
            .ifPresentOrElse(
                member -> member.updateProfile(name, picture),
                () -> memberRepository.save(Member.ofGoogle(
                    sub, name, picture,
                    passwordEncoder.encode(UUID.randomUUID().toString())))
            );

        return oidcUser;
    }
}
