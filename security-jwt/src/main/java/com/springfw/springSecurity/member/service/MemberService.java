package com.springfw.springSecurity.member.service;

import com.springfw.springSecurity.member.MemberRepository;
import com.springfw.springSecurity.member.controller.SignupRequest;
import com.springfw.springSecurity.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signup(SignupRequest req) {
        if (memberRepository.existsById(req.memId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        String encoded = passwordEncoder.encode(req.password());   // {bcrypt}$2a$10$...
        return memberRepository.save(new Member(req.memId(), req.memNm(), encoded)).getMemId();
    }
}
