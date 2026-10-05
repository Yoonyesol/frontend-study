package com.springfw03_jpa_talktalk.board;
import java.time.LocalDateTime;

/**
 * 게시글 상세 응답 DTO (본문 포함)
 */
public record BoardResponseDto(
        Long id,
        String memId,
        String title,
        String text,
        Long viewCount,
        LocalDateTime regDtm,
        LocalDateTime modDtm
) {
    public static BoardResponseDto from(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getMemId(),
                board.getTitle(),
                board.getText(),
                board.getViewCount(),
                board.getRegDtm(),
                board.getModDtm()
        );
    }
}
