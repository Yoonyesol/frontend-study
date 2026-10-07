package com.springfw.springSecurity.config;

import com.springfw.springSecurity.auth.JwtAuthenticationFilter;
import com.springfw.springSecurity.auth.JwtProvider;
import com.springfw.springSecurity.oauth.CustomOidcUserService;
import com.springfw.springSecurity.oauth.OAuth2FailureHandler;
import com.springfw.springSecurity.oauth.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * JWT + Google 소셜 로그인 설정
 * - 아이디/비밀번호 로그인: POST /api/auth/login → JWT 발급
 * - Google 로그인: GET /oauth2/authorization/google → Google → /login/oauth2/code/google → JWT 발급
 * - 이후 모든 API: Authorization: Bearer <JWT> (두 로그인 방식 모두 같은 JWT)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtProvider jwtProvider,
                                           CustomOidcUserService customOidcUserService,
                                           OAuth2SuccessHandler oAuth2SuccessHandler,
                                           OAuth2FailureHandler oAuth2FailureHandler) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            // STATELESS: SecurityContext를 세션에 저장하지 않는다.
            // (OAuth 로그인 도중 state 값은 세션에 잠깐 보관되며, 성공 핸들러에서 정리한다)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(f -> f.disable())
            .httpBasic(b -> b.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/signup", "/error").permitAll()
                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()   // Google 로그인 시작·콜백
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())

            // Google 로그인 (OAuth2 Client)
            .oauth2Login(oauth -> oauth
                .userInfoEndpoint(u -> u.oidcUserService(customOidcUserService))   // 회원 조회·자동 가입
                .successHandler(oAuth2SuccessHandler)                             // JWT 발급 후 React로
                .failureHandler(oAuth2FailureHandler))

            // API는 로그인 페이지로 리다이렉트하지 않고 상태 코드로 응답
            .exceptionHandling(e -> e
                .authenticationEntryPoint((req, res, ex) -> res.sendError(401, "인증 필요"))
                .accessDeniedHandler((req, res, ex) -> res.sendError(403, "권한 없음")))

            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
                             UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
            .role("ADMIN").implies("USER")
            .build();
    }
}
