package com.springfw03_jpa_talktalk.member;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("/login")
    public String login(
            @RequestParam String memId,
            @RequestParam String password,
            HttpSession session
    ){
        if ("lee".equals(memId) && "1234".equals(password)){
            session.setAttribute("memId",memId);
            return "로그인 성공";
        }
        else return "로그인 실패";
    }

    @GetMapping("/mypage")
    public String mypage(HttpSession session){
        String memId = (String)session.getAttribute("memId");
        if (memId == null) return "로그인 필요합니다.";
        else return memId+ "님의 마이페이지";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "로그 아웃 성공";
    }

@GetMapping("/login-check")
public ResponseEntity<?> loginCheck(HttpSession session) {

    String memId = (String) session.getAttribute("memId");

    if (memId == null) {
        return ResponseEntity.status(401)
                .body("로그인이 필요합니다.");
    }

    return ResponseEntity.ok(memId);
    }
}