package com.springfw03_jpa_talktalk.member;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 회원 API에서 사용하는 요청/응답 DTO 모음
 * - Entity(Member)를 API에 그대로 노출하지 않기 위해 사용
 * - Java record: 필드, 생성자, getter(memId()), equals/hashCode/toString 자동 생성
 */
public class MemberDto {

    // ===== 요청 DTO =====

    /** 회원 가입: POST /api/members */
    public record JoinRequest(
            @NotBlank(message = "아이디를 입력하세요.")
            @Pattern(regexp = "^[a-z0-9]{4,50}$", message = "아이디는 영문 소문자와 숫자 4~50자로 입력하세요.")
            String memId,

            @NotBlank(message = "이름을 입력하세요.")
            @Size(max = 50, message = "이름은 50자 이하로 입력하세요.")
            String memNm,

            @NotBlank(message = "비밀번호를 입력하세요.")
            @Size(min = 4, max = 20, message = "비밀번호는 4~20자로 입력하세요.")
            String pwd,

            @Size(max = 255, message = "프로필 이미지 경로는 255자 이하로 입력하세요.")
            String profileImg        // 선택 입력
    ) { }

    /** 회원 정보 수정: PUT /api/members/{memId} */
    public record UpdateRequest(
            @NotBlank(message = "이름을 입력하세요.")
            @Size(max = 50, message = "이름은 50자 이하로 입력하세요.")
            String memNm,

            @Size(max = 255, message = "프로필 이미지 경로는 255자 이하로 입력하세요.")
            String profileImg
    ) { }

    /** 비밀번호 변경: PATCH /api/members/{memId}/password */
    public record PasswordRequest(
            @NotBlank(message = "현재 비밀번호를 입력하세요.")
            String currentPwd,

            @NotBlank(message = "새 비밀번호를 입력하세요.")
            @Size(min = 4, max = 20, message = "비밀번호는 4~20자로 입력하세요.")
            String newPwd
    ) { }

    /** 회원 구분 변경: PATCH /api/members/{memId}/cd */
    public record CdRequest(
            @NotBlank(message = "회원 구분을 입력하세요.")
            @Pattern(regexp = "^[01]$", message = "회원 구분은 0(일반) 또는 1(관리자)이어야 합니다.")
            String memCd             // DB의 CHECK (MEM_CD IN ('0','1'))와 같은 규칙
    ) { }

    // ===== 응답 DTO =====

    /** 회원 조회 응답 — 비밀번호(pwd)는 절대 포함하지 않음 */
    public record Response(
            String memId,
            String memNm,
            String memCd,
            String profileImg
    ) {
        // Entity → DTO 변환 (정적 팩토리 메서드)
        public static Response from(Member member) {
            return new Response(
                    member.getMemId(),
                    member.getMemNm(),
                    member.getMemCd(),
                    member.getProfileImg()
            );
        }
    }
}