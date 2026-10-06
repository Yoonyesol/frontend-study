-- SQL Developer 등에서 scott 계정으로 직접 실행합니다.
-- (ddl-auto: none 이고 Oracle은 내장 DB가 아니므로 애플리케이션이 자동 실행하지 않음)

DROP TABLE tb_mem2;   -- 처음 실행 시 테이블이 없으면 에러가 나지만 무시해도 됨

CREATE TABLE tb_mem2 (
    mem_id       VARCHAR2(50)  PRIMARY KEY,
    mem_nm       VARCHAR2(50)  NOT NULL,
    mem_cd       CHAR(1)       DEFAULT '0' CHECK (mem_cd IN ('0', '1')),  -- 0 일반사용자, 1 관리자
    pwd          VARCHAR2(100) NOT NULL,                                -- {bcrypt} 포함 해시 68자
    profile_img  VARCHAR2(255)
);

-- 초기 데이터: {noop}은 '암호화하지 않은 값'이라는 표시 (실습용)
INSERT INTO tb_mem2 VALUES ('kim', '김미연', '0', '{noop}1234', 'a.png');
INSERT INTO tb_mem2 VALUES ('lee', '이철수', '1', '{noop}1234', 'b.png');
COMMIT;
