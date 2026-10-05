package com.springfw03_jpa_talktalk.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 게시글 등록/수정 요청 DTO
 * - 수정 시에는 memId 를 사용하지 않음
 */
public record BoardRequestDto(
        @Size(max = 10, message = "회원 ID는 10자 이하입니다.")
        String memId,

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 200, message = "제목은 200자 이하입니다.")
        String title,

        String text
)
{
    public Board toEntity() {
        return new Board(memId, title, text);
    }
}
