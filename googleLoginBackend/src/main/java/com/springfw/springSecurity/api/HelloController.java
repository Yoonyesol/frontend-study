package com.springfw.springSecurity.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 토큰 테스트용: 로그인한 사용자 누구나 접근
 */
@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String hello(Authentication auth) {
        return auth.getName() + "님 안녕하세요. 권한: " + auth.getAuthorities();
    }
}
