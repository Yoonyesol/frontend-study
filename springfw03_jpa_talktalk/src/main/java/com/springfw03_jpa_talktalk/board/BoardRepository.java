package com.springfw03_jpa_talktalk.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 삭제되지 않은 글 목록 (페이징)
    // Oracle 12c+ 에서는 OFFSET n ROWS FETCH NEXT m ROWS ONLY 로 변환됨
    Page<Board> findByDelFlg(Integer delFlg, Pageable pageable);

    // 삭제되지 않은 글 단건
    Optional<Board> findByIdAndDelFlg(Long id, Integer delFlg);

    // 조회수 1 증가 (UPDATE 한 번으로 처리 → 동시 조회 시에도 누락 없음)
    @Modifying(clearAutomatically = true)
    @Query("update Board b set b.viewCount = b.viewCount + 1 where b.id = :id and b.delFlg = 0")
    int increaseViewCount(@Param("id") Long id);
}