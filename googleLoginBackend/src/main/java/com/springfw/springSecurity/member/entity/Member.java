package com.springfw.springSecurity.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_mem2")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    public static final String GOOGLE_PREFIX = "google_";

    @Id
    @Column(name = "mem_id")
    private String memId;          // 아이디가 곧 PK (소셜 회원은 google_ + Google sub)

    @Column(name = "mem_nm", nullable = false)
    private String memNm;

    @Column(name = "mem_cd")
    private String memCd;          // "0" 일반사용자, "1" 관리자

    @Column(name = "pwd", nullable = false)
    private String pwd;            // 반드시 암호화된 값

    @Column(name = "profile_img")
    private String profileImg;     // 일반 회원: 파일명, 소셜 회원: Google 프로필 이미지 URL

    // 일반 회원가입
    public Member(String memId, String memNm, String encodedPwd) {
        this(memId, memNm, encodedPwd, null);
    }

    private Member(String memId, String memNm, String encodedPwd, String profileImg) {
        this.memId = memId;
        this.memNm = memNm;
        this.pwd = encodedPwd;
        this.profileImg = profileImg;
        this.memCd = "0";          // 가입은 항상 일반사용자
    }

    // Google 첫 로그인 시 자동 가입
    // 비밀번호는 아무도 모르는 임의 값의 해시 → 아이디/비밀번호 로그인은 불가능
    public static Member ofGoogle(String sub, String name, String pictureUrl, String encodedRandomPwd) {
        return new Member(GOOGLE_PREFIX + sub, name, encodedRandomPwd, truncate(pictureUrl, 255));
    }

    // Google에서 이름·사진이 바뀌었으면 로그인할 때 반영 (JPA 변경 감지로 UPDATE)
    public void updateProfile(String name, String pictureUrl) {
        this.memNm = name;
        this.profileImg = truncate(pictureUrl, 255);
    }

    public boolean isSocial() {
        return memId.startsWith(GOOGLE_PREFIX);
    }

    // DB 코드값 → Spring Security 역할 이름
    public String getRoleName() {
        return "1".equals(memCd) ? "ADMIN" : "USER";
    }

    private static String truncate(String value, int max) {
        return (value == null || value.length() <= max) ? value : null;   // 컬럼보다 길면 저장하지 않음
    }
}
