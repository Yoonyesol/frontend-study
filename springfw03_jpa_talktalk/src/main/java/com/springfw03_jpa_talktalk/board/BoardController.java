package com.springfw03_jpa_talktalk.board;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 목록: GET /api/boards?page=1
    @GetMapping
    public PagedModel<BoardListResponseDto> list(@RequestParam(defaultValue = "1") int page) {
        return new PagedModel<>(boardService.getList(page));
    }

    // 상세: GET /api/boards/{id}  (조회수 +1)
    @GetMapping("/{id}")
    public BoardResponseDto detail(@PathVariable Long id) {
        return boardService.getDetail(id);
    }

    // 등록: POST /api/boards
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody BoardRequestDto request) {
        Long id = boardService.create(request);
        return ResponseEntity.created(URI.create("/api/boards/" + id)).build();
    }

    // 수정: PUT /api/boards/{id}
    @PutMapping("/{id}")
    public BoardResponseDto update(@PathVariable Long id,
                                   @Valid @RequestBody BoardRequestDto request) {
        return boardService.update(id, request);
    }

    // 삭제: DELETE /api/boards/{id}  (소프트 삭제)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 게시글 없음 → 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleNotFound(EntityNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
}

