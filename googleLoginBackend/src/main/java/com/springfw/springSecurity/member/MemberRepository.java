package com.springfw.springSecurity.member;

import com.springfw.springSecurity.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {   // PK 타입이 String

    Optional<Member> findByMemId(String memId);   // 반환 타입은 Optional (orElseThrow 사용)
}
