package com.example.springSecurity.member.controller;

import com.example.springSecurity.member.MemberRepository;
import com.example.springSecurity.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * React에서 호출하는 인증 보조 API
 * - 로그인(POST /api/login), 로그아웃(POST /api/logout)은 Spring Security 필터가 처리하므로 여기 없음
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthApiController {

    private final MemberRepository memberRepository;

    // CSRF 토큰을 로드해서 XSRF-TOKEN 쿠키가 발급되게 한다.
    @GetMapping("/csrf")
    public ResponseEntity<Void> csrf(CsrfToken token) {
        token.getToken();   // 토큰을 실제로 꺼내야 쿠키로 저장됨
        return ResponseEntity.noContent().build();
    }

    // 현재 로그인 사용자 정보 (로그인 전이면 401)
    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        Member member = memberRepository.findByMemId(authentication.getName()).orElseThrow();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Map.of(
                "memId", member.getMemId(),
                "memNm", member.getMemNm(),
                "roles", roles
        );
    }
}
