package com.springfw03_jpa_talktalk.board;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="TB_Board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq_gen")
    @SequenceGenerator(name="board_seq_gen", sequenceName = "seq_tb_board_id", allocationSize = 1)
    private Long id;

    // TB_MEM(MEM_ID) FK. 회원 엔티티 없이 문자열로 매핑 (필요 시 @ManyToOne 으로 확장)
    @Column(name = "MEM_ID", length = 10)
    private String memId;

    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @Lob
    @Column(name = "TEXT")
    private String text;

    // 컬럼명 COUNT → 필드명은 의미가 드러나게 viewCount
    @Column(name = "COUNT")
    private Long viewCount = 0L;

    // 0: 정상, 1: 삭제
    @Column(name = "DEL_FLG")
    private Integer delFlg = 0;

    @Column(name = "REG_DTM", updatable = false)
    private LocalDateTime regDtm;

    @Column(name = "MOD_DTM")
    private LocalDateTime modDtm;

    public Board(String memId, String title, String text) {
        this.memId = memId;
        this.title = title;
        this.text = text;
    }

    @PrePersist
    void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        this.regDtm = now;
        this.modDtm = now;
    }
    @PreUpdate
    void onUpdate(){
        this.modDtm = LocalDateTime.now();
    }

    // ===== 비즈니스 메서드 =====

    public void update(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public void delete() {
        this.delFlg = 1;
    }

    public boolean isDeleted() {
        return this.delFlg != null && this.delFlg == 1;
    }
}
