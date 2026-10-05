package com.springfw03_jpa_talktalk.board;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private static final int PAGE_SIZE = 10;
    private static final int NOT_DELETED = 0;

    private final BoardRepository boardRepository;

    /**
     * 목록 조회 - 최신글 순, 한 페이지 10개
     * @param page 1부터 시작하는 페이지 번호
     */
    public Page<BoardListResponseDto> getList(int page) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "id"));
        return boardRepository.findByDelFlg(NOT_DELETED, pageable)
                .map(BoardListResponseDto::from);
    }

    /**
     * 상세 조회 - 조회수 1 증가 후 조회
     */
    @Transactional
    public BoardResponseDto getDetail(Long id) {
        boardRepository.increaseViewCount(id);
        return BoardResponseDto.from(findActive(id));
    }

    @Transactional
    public Long create(BoardRequestDto request) {
        return boardRepository.save(request.toEntity()).getId();
    }

    /**
     * 수정 - 변경 감지(dirty checking)로 UPDATE
     */
    @Transactional
    public BoardResponseDto update(Long id, BoardRequestDto request) {
        Board board = findActive(id);
        board.update(request.title(), request.text());
        return BoardResponseDto.from(board);
    }

    /**
     * 삭제 - DEL_FLG = 1 (소프트 삭제)
     */
    @Transactional
    public void delete(Long id) {
        findActive(id).delete();
    }

    private Board findActive(Long id) {
        return boardRepository.findByIdAndDelFlg(id, NOT_DELETED)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. id=" + id));
    }
}
