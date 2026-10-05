package com.springfw03_jpa_talktalk.member;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 구현 클래스 없음 — Spring Data JPA가 런타임에 자동 생성
public interface MemberRepository extends JpaRepository<Member, String> {

    // 쿼리 메서드: WHERE MEM_NM LIKE '%?%' ORDER BY MEM_ID
    List<Member> findByMemNmContainingOrderByMemId(String keyword);

    // WHERE MEM_CD = ?
    List<Member> findByMemCd(String memCd);
}
