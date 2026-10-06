package com.example.springSecurity.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_mem2")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    public static final String CD_USER  = "0";   // 일반 회원
    public static final String CD_ADMIN = "1";   // 관리자

    @Id
    @Column(name = "mem_id")
    private String memId;          // 아이디가 곧 PK (자동 생성 아님)

    @Column(name = "mem_nm", nullable = false)
    private String memNm;

    @Column(name = "mem_cd")
    private String memCd;          // "0" 일반사용자, "1" 관리자

    @Column(name = "pwd", nullable = false)
    private String pwd;            // 반드시 암호화된 값

    @Column(name = "profile_img")
    private String profileImg;

    public Member(String memId, String memNm, String encodedPwd) {
        this.memId = memId;
        this.memNm = memNm;
        this.pwd = encodedPwd;
        this.memCd = CD_USER;         // 회원가입은 항상 일반사용자
    }

    // DB 코드값 → Spring Security 역할 이름
    public String getRoleName() {                                   // SpringSecurity에서 호출
        return CD_ADMIN.equals(memCd) ? "ADMIN" : "USER";
    }


    // ===== 도메인 로직 (Setter 대신) =====
    public void changeInfo(String memNm, String profileImg) {
        this.memNm = memNm;
        this.profileImg = profileImg;
    }

    public void changePassword(String currentPwd, String newPwd) {
        if (!this.pwd.equals(currentPwd)) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        this.pwd = newPwd;
    }

    public void changeCd(String memCd) {
        this.memCd = memCd;
    }

    public boolean isAdmin() {
        return CD_ADMIN.equals(memCd);
    }
}

