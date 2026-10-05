package com.springfw03_jpa_talktalk.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity   // JPA가 관리하는 클래스
@Table(name = "TB_MEM")  // 매핑할 테이블 이름
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // JPA용 기본 생성자
public class Member {
    public static final String CD_USER  = "0";   // 일반 회원
    public static final String CD_ADMIN = "1";   // 관리자

    @Id  // PK
    @Column(name = "MEM_ID", length = 50)
    private String memId;                          // 사용자가 입력하는 ID → @GeneratedValue 없음

    @Column(name = "MEM_NM", length = 50, nullable = false)
    private String memNm;

    @Column(name = "MEM_CD", columnDefinition = "CHAR(1)")
    private String memCd;

    @Column(name = "PWD", length = 20)
    private String pwd;

    @Column(name = "PROFILE_IMG", length = 255)
    private String profileImg;

    public Member(String memId, String memNm, String pwd, String profileImg) {
        this.memId = memId;
        this.memNm = memNm;
        this.pwd = pwd;
        this.profileImg = profileImg;
        this.memCd = CD_USER;                      // 가입 시 기본값: 일반 회원
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
