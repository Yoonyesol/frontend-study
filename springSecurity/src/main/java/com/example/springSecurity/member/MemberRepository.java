package com.example.springSecurity.member;

import java.util.Optional;

import com.example.springSecurity.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {  // PK 타입이 String
    Optional<Member> findByMemId(String memId);
}