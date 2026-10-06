package com.springfw.springSecurity.member.controller;

import com.springfw.springSecurity.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 회원가입 API (토큰 없이 호출 가능: SecurityConfig에서 permitAll)
 */
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // POST /signup  {"memId":"park","memNm":"박지성","password":"1234"}
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody SignupRequest req) {
        String memId = memberService.signup(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("memId", memId));
    }
}
