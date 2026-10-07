package com.springfw.springSecurity.oauth;

import com.springfw.springSecurity.auth.JwtProvider;
import com.springfw.springSecurity.member.MemberRepository;
import com.springfw.springSecurity.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Google 로그인 성공 → 우리 서비스의 JWT를 발급해 React로 돌려보낸다.
 *
 * 여기서 Google의 토큰을 그대로 쓰지 않는 이유:
 * Google 토큰은 "Google 계정" 증명일 뿐, 우리 서비스의 권한(ROLE_ADMIN 등)을 담고 있지 않다.
 * 그래서 우리 회원 정보(tb_mem)로 우리 JWT를 새로 만든다. → 이후 API는 아이디 로그인과 완전히 같은 흐름
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        Member member = memberRepository.findByMemId(Member.GOOGLE_PREFIX + oidcUser.getSubject())
            .orElseThrow();   // CustomOidcUserService에서 이미 저장했으므로 반드시 존재

        // 아이디 로그인과 같은 형태의 인증 객체로 바꿔서 같은 메서드로 토큰 생성
        Authentication ourAuth = new UsernamePasswordAuthenticationToken(
            member.getMemId(), null,
            List.of(new SimpleGrantedAuthority("ROLE_" + member.getRoleName())));
        String token = jwtProvider.createAccessToken(ourAuth);

        // OAuth 로그인 과정에서만 잠깐 쓴 세션 정리 (state 값 보관용)
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        // URL의 # 뒤(fragment)는 서버로 전송되지 않아 서버 로그·Referer에 토큰이 남지 않는다.
        response.sendRedirect(frontendUrl + "/oauth/callback#token=" + token);
    }
}
