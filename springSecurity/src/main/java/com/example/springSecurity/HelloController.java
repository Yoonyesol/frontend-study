package com.example.springSecurity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {

        // 방법 1: 컨트롤러 파라미터로 주입
        // 방법 2: SecurityContextHolder.getContext().getAuthentication()

        return Map.of(   // map으로 리턴해도 json 으로 반환
                "name", authentication.getName(),
                "authorities", authentication.getAuthorities()
        );
    }

}


