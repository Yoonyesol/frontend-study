package com.springfw.springSecurity.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Google 로그인 실패(사용자가 동의 화면에서 취소, client-secret 오류 등) → React 로그인 화면으로
 */
@Slf4j
@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.warn("Google 로그인 실패: {}", exception.getMessage());   // 원인은 서버 로그에서 확인
        response.sendRedirect(frontendUrl + "/oauth/callback#error=social_login_failed");
    }
}
