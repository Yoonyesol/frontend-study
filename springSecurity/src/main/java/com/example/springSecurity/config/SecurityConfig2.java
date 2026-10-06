package com.example.springSecurity.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

/**
 * React(SPA) 로그인용 설정
 * - 세션 방식은 그대로 유지하되, 화면 이동(redirect) 대신 HTTP 상태 코드로 응답한다.
 * - CSRF 토큰은 쿠키(XSRF-TOKEN)로 내려주고 헤더(X-XSRF-TOKEN)로 돌려받는다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig2 {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/csrf", "/api/login", "/signup", "/error").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                // 폼 로그인 처리는 Spring Security가 그대로 담당, 응답만 상태 코드로 변경
                .formLogin(form -> form
                        .loginProcessingUrl("/api/login")      // React가 POST하는 주소
                        .usernameParameter("memId")
                        .passwordParameter("password")
                        .successHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))            // 200
                        .failureHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED)))   // 401

                // 로그아웃: 리다이렉트 대신 200
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .deleteCookies("JSESSIONID"))

                // 로그인 안 한 상태로 API 호출 시 로그인 페이지로 302 대신 401
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

                // SPA용 CSRF: XSRF-TOKEN 쿠키 발급 + X-XSRF-TOKEN 헤더 검증
                .csrf(csrf -> csrf
                        .spa()
                        .ignoringRequestMatchers("/signup"));  // /signup은 Postman 실습용 예외 (화면으로만 가입시키면 지워도 됨)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
