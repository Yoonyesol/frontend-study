package com.example.springSecurity.member.service;

import com.example.springSecurity.member.entity.Member;
import com.example.springSecurity.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Member member = memberRepository.findByMemId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + username));

        return User.withUsername(member.getMemId())
                .password(member.getPwd())
                .roles(member.getRoleName())   // "1" → ROLE_ADMIN, "0" → ROLE_USER
                .build();
    }
}