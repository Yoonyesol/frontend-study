package com.example.springSecurity.member.service;

import com.example.springSecurity.member.MemberRepository;
import com.example.springSecurity.member.entity.Member;
import com.example.springSecurity.member.dto.SignupRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signup(SignupRequest req) {
        if (memberRepository.existsById(req.memId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디");
        }
        String encoded = passwordEncoder.encode(req.password());
        return memberRepository.save(new Member(req.memId(), req.memNm(), encoded)).getMemId();
    }
}
