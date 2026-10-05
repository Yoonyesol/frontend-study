package com.springfw03_jpa_talktalk.board;

import java.time.LocalDateTime;

/**
 * 게시글 목록 응답 DTO (본문 CLOB 제외)
 */
public record BoardListResponseDto(
        Long id,
        String memId,
        String title,
        Long viewCount,
        LocalDateTime regDtm
) {
    public static BoardListResponseDto from(Board board) {
        return new BoardListResponseDto(
                board.getId(),
                board.getMemId(),
                board.getTitle(),
                board.getViewCount(),
                board.getRegDtm()
        );
    }
}
